package com.example.cluvrapi.domain.auth.service;

import java.time.Duration;
import java.security.SecureRandom;
import java.time.LocalDateTime;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.cluvrapi.domain.auth.dto.request.LoginUserRequestDto;
import com.example.cluvrapi.domain.auth.dto.request.SignUpUserRequestDto;
import com.example.cluvrapi.domain.auth.dto.request.SignUpVerifyCacheRequestDto;
import com.example.cluvrapi.domain.auth.dto.request.SignUpVerifyRequestDto;
import com.example.cluvrapi.domain.auth.dto.response.LoginUserResponseDto;
import com.example.cluvrapi.domain.auth.dto.response.SignUpUserResponseDto;
import com.example.cluvrapi.domain.auth.properties.AppProperties;
import com.example.cluvrapi.domain.category.entity.Category;
import com.example.cluvrapi.domain.category.enums.CategoryTargetType;
import com.example.cluvrapi.domain.category.repository.CategoryRepository;
import com.example.cluvrapi.domain.clover.dto.request.CreateCloverRequestDto;
import com.example.cluvrapi.domain.clover.enums.Tier;
import com.example.cluvrapi.domain.clover.service.CloverService;
import com.example.cluvrapi.domain.gem.dto.request.UpdateGemRequestDto;
import com.example.cluvrapi.domain.gem.enums.GemUserActivityType;
import com.example.cluvrapi.domain.gem.service.GemEvent;
import com.example.cluvrapi.domain.gem.service.GemService;
import com.example.cluvrapi.domain.user.entity.User;
import com.example.cluvrapi.domain.user.entity.enums.UserRole;
import com.example.cluvrapi.domain.user.repository.UserRepository;
import com.example.cluvrapi.global.annotation.EventGem;
import com.example.cluvrapi.global.exception.BusinessException;
import com.example.cluvrapi.global.jwt.CustomUserDetails;
import com.example.cluvrapi.global.jwt.JwtUtil;
import com.example.cluvrapi.global.jwt.RefreshTokenServiceImpl;
import com.example.cluvrapi.global.response.ResponseCode;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepository;
	private final AuthenticationManager authenticationManager;
	private final JwtUtil jwtUtil;
	private final RefreshTokenServiceImpl refreshTokenService;
	private final CategoryRepository categoryRepository;
	private final CloverService cloverService;
	private final AppProperties appProperties;
	private final RedisTemplate<String, Object> redisTemplate;
	private final SecureRandom random = new SecureRandom();
	private final ApplicationEventPublisher publisher; // 이벤트 발행
	private final GemService gemService;

	private final JavaMailSender mailSender;

	private static final Duration VERIFY_TTL = Duration.ofMinutes(10);

	@Value("${spring.mail.username}")
	private String mailFrom;

	@Override
	public void signUp(SignUpUserRequestDto dto) {
		if (userRepository.existsByEmail(dto.getEmail())) {
			throw new BusinessException(ResponseCode.INVALID_REQUEST, "이미 사용 중인 이메일입니다.");
		}
		if (userRepository.existsByPhoneNumber(dto.getPhoneNumber())) {
			throw new BusinessException(ResponseCode.INVALID_REQUEST, "이미 등록된 전화번호입니다.");
		}
		if (!dto.getPassword().equals(dto.getConfirmPassword())) {
			throw new BusinessException(ResponseCode.INVALID_REQUEST, "비밀번호와 확인이 일치하지 않습니다.");
		}

		String code = String.format("%06d", random.nextInt(900_000) + 100_000);
		String emailLower = dto.getEmail().toLowerCase();
		String key = "signup:verify:" + emailLower;
		SignUpVerifyCacheRequestDto cacheDto = new SignUpVerifyCacheRequestDto(dto, code);

		redisTemplate.opsForValue().set(key, cacheDto, VERIFY_TTL);
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(emailLower);
		msg.setSubject("【Cluvr】 회원가입 인증번호 안내");
		msg.setText(
			"안녕하세요, Cluvr입니다.\n\n" +
				"회원가입을 위해 아래 인증번호를 입력해 주세요:\n\n" +
				code + "\n\n" +
				"※ 유효시간: 10분\n" +
				"감사합니다."
		);
		try {
			mailSender.send(msg);
		} catch (Exception e) {
			// 캐시 삭제
			redisTemplate.delete(key);
			throw new BusinessException(ResponseCode.DB_FAIL, "이메일 발송에 실패했습니다. 잠시 후 다시 시도해주세요.");
		}
	}

	@Override
	@Transactional
	public LoginUserResponseDto login(LoginUserRequestDto requestDto) {

		try {
			// 1) 이메일·비밀번호 인증 시도
			Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(requestDto.getEmail(), requestDto.getPassword()));

			// 인증 성공 시 principal은 CustomUserDetails
			User user = ((CustomUserDetails)authentication.getPrincipal()).getUser();
			Long userId = user.getId();

			if (user.getIsDeleted()) {
				throw new BusinessException(
					ResponseCode.WITHDRAWN_USER_ACCESS,
					"탈퇴한 유저는 접근할 수 없습니다."
				);
			}
			// 2) 액세스 토큰 생성
			String accessToken = jwtUtil.generateAccessToken(userId, user.getUserRole().name());

			// 3) 리프레시 토큰 생성 및 저장 (Redis 등)
			String refreshToken = refreshTokenService.createRefreshToken(userId, user.getUserRole().name());

			// 3.5) Gem 적립
			getReward(userId);

			// 4) DTO 변환 후 반환
			return LoginUserResponseDto.from(user, accessToken, refreshToken);
		} catch (BadCredentialsException ex) {
			throw new BusinessException(ResponseCode.LOGIN_FAILED);
		}
	}

	@Override
	public void logout(String accessToken) {

		if (accessToken == null || accessToken.isBlank()) {
			throw new BusinessException(
				ResponseCode.AUTH_REQUIRED,
				"로그아웃할 토큰이 제공되지 않았습니다."
			);
		}
		try {
			long expireMillis = jwtUtil.getExpirationMillis(accessToken);
			long remainingMillis = expireMillis - System.currentTimeMillis();
			if (remainingMillis > 0) {
				refreshTokenService.blacklistAccessToken(accessToken, remainingMillis);
			}
		} catch (io.jsonwebtoken.JwtException ex) {
			throw new BusinessException(
				ResponseCode.TOKEN_INVALID,
				"유효하지 않은 액세스 토큰입니다."
			);
		} catch (Exception ex) {
			throw new BusinessException(
				ResponseCode.DB_FAIL,
				"로그아웃 처리 중 오류가 발생했습니다."
			);
		}

	}

	@Override
	@Transactional
	public SignUpUserResponseDto completeSignUp(SignUpVerifyRequestDto req) {
		String email = req.getEmail().toLowerCase();
		String key = "signup:verify:" + email;

		Object cachedObject = redisTemplate.opsForValue().get(key);
		if (cachedObject == null) {
			throw new BusinessException(ResponseCode.INVALID_REQUEST, "인증 요청이 없거나 만료되었습니다.");
		}
		if (!(cachedObject instanceof SignUpVerifyCacheRequestDto)) {
			throw new BusinessException(ResponseCode.DB_FAIL, "잘못된 캐시 데이터입니다.");
		}
		SignUpVerifyCacheRequestDto cache = (SignUpVerifyCacheRequestDto)cachedObject;
		if (cache == null) {
			throw new BusinessException(ResponseCode.INVALID_REQUEST, "인증 요청이 없거나 만료되었습니다.");
		}
		if (!cache.getVerificationCode().equals(req.getCode())) {
			throw new BusinessException(ResponseCode.INVALID_REQUEST, "인증번호가 일치하지 않습니다.");
		}

		redisTemplate.delete(key);

		SignUpUserRequestDto dto = cache.getSignUpRequest();
		String emailLower = dto.getEmail().toLowerCase();
		String domain = emailLower.substring(emailLower.indexOf('@') + 1);
		UserRole role = appProperties.getAdminDomains().contains(domain)
			? UserRole.ADMIN : UserRole.USER;

		User user = new User(
			null,
			dto.getName(),
			dto.getBirthday(),
			emailLower,
			dto.getPhoneNumber(),
			role,
			dto.getGender(),
			dto.getCategoryType(),
			passwordEncoder.encode(dto.getPassword()),
			0,
			dto.getImageUrl(),
			false
		);
		User saved = userRepository.save(user);

		categoryRepository.save(new Category(
			saved.getId(),
			dto.getCategoryType(),
			CategoryTargetType.USER
		));
		cloverService.createClover(
			CreateCloverRequestDto.from(0, Tier.SPROUT, saved.getId())
		);

		return SignUpUserResponseDto.from(saved);
	}

	private void getReward(Long userId) {
		GemUserActivityType gemUserActivityType = GemUserActivityType.LOGIN;
		Integer gem = gemUserActivityType.getGem();
		gemService.earnGems(userId, UpdateGemRequestDto.from(gem, gemUserActivityType));
		publisher.publishEvent(
			GemEvent.createEvent(userId, gem, gemUserActivityType.getDescription(), LocalDateTime.now(), null,
				gemUserActivityType.getFlowType(), gemUserActivityType.name())
		);
	}

}


