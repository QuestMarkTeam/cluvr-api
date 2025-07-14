package com.example.cluvrapi.domain.auth.service;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.example.cluvrapi.domain.auth.dto.request.CompleteProfileRequestDto;
import com.example.cluvrapi.domain.auth.dto.request.LoginUserRequestDto;
import com.example.cluvrapi.domain.auth.dto.request.SignUpUserRequestDto;
import com.example.cluvrapi.domain.auth.dto.request.SignUpVerifyCacheRequestDto;
import com.example.cluvrapi.domain.auth.dto.request.SignUpVerifyRequestDto;
import com.example.cluvrapi.domain.auth.dto.request.SocialSignupRequestDto;
import com.example.cluvrapi.domain.auth.dto.response.LoginUserResponseDto;
import com.example.cluvrapi.domain.auth.dto.response.SignUpUserResponseDto;
import com.example.cluvrapi.domain.auth.dto.response.SocialLoginResponseDto;
import com.example.cluvrapi.domain.auth.properties.AppProperties;
import com.example.cluvrapi.domain.category.entity.Category;
import com.example.cluvrapi.domain.category.enums.CategoryTargetType;
import com.example.cluvrapi.domain.category.enums.CategoryType;
import com.example.cluvrapi.domain.category.repository.CategoryRepository;
import com.example.cluvrapi.domain.clover.dto.request.CreateCloverRequestDto;
import com.example.cluvrapi.domain.clover.enums.Tier;
import com.example.cluvrapi.domain.clover.service.CloverService;
import com.example.cluvrapi.domain.gem.dto.request.UpdateGemRequestDto;
import com.example.cluvrapi.domain.gem.enums.GemUserActivityType;
import com.example.cluvrapi.domain.gem.service.GemEvent;
import com.example.cluvrapi.domain.gem.service.GemService;
import com.example.cluvrapi.domain.image.entity.ImageType;
import com.example.cluvrapi.domain.image.service.ImageService;
import com.example.cluvrapi.domain.user.entity.User;
import com.example.cluvrapi.domain.user.entity.enums.Gender;
import com.example.cluvrapi.domain.user.entity.enums.UserRole;
import com.example.cluvrapi.domain.user.repository.UserRepository;
import com.example.cluvrapi.global.exception.BusinessException;
import com.example.cluvrapi.global.jwt.JwtUtil;
import com.example.cluvrapi.global.jwt.RefreshTokenService;
import com.example.cluvrapi.global.response.ResponseCode;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepository;
	private final CategoryRepository categoryRepository;
	private final CloverService cloverService;
	private final AppProperties appProperties;
	private final RedisTemplate<String, Object> redisTemplate;
	private final SecureRandom random = new SecureRandom();
	private final ApplicationEventPublisher publisher; // 이벤트 발행
	private final GemService gemService;
	private final ImageService imageService;
	private final JavaMailSender mailSender;
	private final JwtUtil jwtUtil;
	private final RefreshTokenService refreshTokenService;

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

		// 1. 비밀번호 암호화
		String encodedPassword = passwordEncoder.encode(dto.getPassword());
		// 2. 고유한 sub 생성 (UUID 사용)
		String sub = UUID.randomUUID().toString();

		// 3. 사용자 엔티티 생성 및 저장
		User user = new User(
			null, // ID는 자동 생성
			dto.getName(),
			dto.getBirthday(),
			dto.getEmail(),
			dto.getPhoneNumber(),
			UserRole.USER,
			dto.getGender(),
			dto.getCategoryType(),
			encodedPassword,
			0, // gem 초기값
			null, // imageUrl 초기값
			false, // isDeleted 초기값
			sub
		);
		User savedUser = userRepository.save(user);

		// 4. 카테고리 생성
		Category category = new Category(
			savedUser.getId(),
			dto.getCategoryType(),
			CategoryTargetType.CLUB
		);
		categoryRepository.save(category);

		// 5. 클로버 생성
		CreateCloverRequestDto cloverRequest = new CreateCloverRequestDto(
			0, // 초기 클로버 점수
			Tier.SPROUT, // 초기 등급
			savedUser.getId()
		);
		cloverService.createClover(cloverRequest);

		// 6. 회원가입 보상 지급
		// publisher.publishEvent(GemEvent.createEvent(
		// 	savedUser.getId(),
		// 	GemUserActivityType.SIGNUP.getGem(),
		// 	GemUserActivityType.SIGNUP.getDescription(),
		// 	LocalDateTime.now(),
		// 	null,
		// 	GemUserActivityType.SIGNUP.getFlowType(),
		// 	GemUserActivityType.SIGNUP.name()
		// ));
	}

	@Override
	@Transactional
	public LoginUserResponseDto login(LoginUserRequestDto requestDto) {
		try {
			// 1. 이메일로 사용자 조회
			User user = userRepository.findByEmail(requestDto.getEmail())
				.orElseThrow(() -> new BusinessException(ResponseCode.LOGIN_FAILED, "이메일 또는 비밀번호가 일치하지 않습니다."));

			// 2. 비밀번호 검증
			if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
				throw new BusinessException(ResponseCode.LOGIN_FAILED, "이메일 또는 비밀번호가 일치하지 않습니다.");
			}

			// 3. 탈퇴 사용자 확인
			if (user.getIsDeleted()) {
				throw new BusinessException(
					ResponseCode.WITHDRAWN_USER_ACCESS,
					"탈퇴한 유저는 접근할 수 없습니다."
				);
			}

			Long userId = user.getId();
			String email = user.getEmail();
			String role = user.getUserRole().name();

			// 4. JWT 토큰 생성
			String accessToken = jwtUtil.generateAccessToken(userId, email, role);
			String refreshToken = jwtUtil.generateRefreshToken(userId, email, role);

			// 5. 리프레시 토큰 저장
			refreshTokenService.saveRefreshToken(userId, refreshToken);

			// 6. Gem 적립
			// getReward(userId);

			// 7. DTO 변환 후 반환
			return LoginUserResponseDto.from(
				email,
				user.getName(),
				accessToken,
				refreshToken,
				null // idToken은 더 이상 사용하지 않음
			);

		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			log.error("로그인 중 예외 발생", e);
			throw new BusinessException(ResponseCode.INTERNAL_ERROR);
		}
	}

	@Override
	public void logout(String refreshToken) {
		if (refreshToken == null || refreshToken.isBlank()) {
			throw new BusinessException(
				ResponseCode.AUTH_REQUIRED,
				"로그아웃할 토큰이 제공되지 않았습니다."
			);
		}

		try {
			// 리프레시 토큰에서 사용자 ID 추출
			Long userId = jwtUtil.getUserIdFromToken(refreshToken);
			
			// Redis에서 리프레시 토큰 삭제
			refreshTokenService.deleteRefreshToken(userId);
			
			log.info("로그아웃 완료 - 사용자 ID: {}", userId);
		} catch (Exception e) {
			log.error("로그아웃 처리 중 오류 발생", e);
			throw new BusinessException(ResponseCode.INTERNAL_ERROR, "로그아웃 처리 중 오류가 발생했습니다.");
		}
	}

	// @Override
	// @Transactional
	// public SignUpUserResponseDto completeSignUp(SignUpVerifyRequestDto req) {
	// 	String email = req.getEmail().toLowerCase();
	// 	String key = "signup:verify:" + email;
	//
	// 	Object cachedObject = redisTemplate.opsForValue().get(key);
	// 	if (cachedObject == null) {
	// 		throw new BusinessException(ResponseCode.INVALID_REQUEST, "인증 요청이 없거나 만료되었습니다.");
	// 	}
	// 	if (!(cachedObject instanceof SignUpVerifyCacheRequestDto)) {
	// 		throw new BusinessException(ResponseCode.DB_FAIL, "잘못된 캐시 데이터입니다.");
	// 	}
	// 	SignUpVerifyCacheRequestDto cache = (SignUpVerifyCacheRequestDto)cachedObject;
	// 	if (cache == null) {
	// 		throw new BusinessException(ResponseCode.INVALID_REQUEST, "인증 요청이 없거나 만료되었습니다.");
	// 	}
	// 	if (!cache.getVerificationCode().equals(req.getCode())) {
	// 		throw new BusinessException(ResponseCode.INVALID_REQUEST, "인증번호가 일치하지 않습니다.");
	// 	}
	//
	// 	redisTemplate.delete(key);
	//
	// 	SignUpUserRequestDto dto = cache.getSignUpRequest();
	//
	// 	// 1. 비밀번호 암호화
	// 	String encodedPassword = passwordEncoder.encode(dto.getPassword());
	//
	// 	// 2. 고유한 sub 생성 (UUID 사용)
	// 	String sub = UUID.randomUUID().toString();
	//
	// 	// 3. 사용자 엔티티 생성 및 저장
	// 	User user = new User(
	// 		null, // ID는 자동 생성
	// 		dto.getName(),
	// 		dto.getBirthday(),
	// 		dto.getEmail(),
	// 		dto.getPhoneNumber(),
	// 		UserRole.USER,
	// 		dto.getGender(),
	// 		dto.getCategoryType(),
	// 		encodedPassword,
	// 		0, // gem 초기값
	// 		null, // imageUrl 초기값
	// 		false, // isDeleted 초기값
	// 		sub
	// 	);
	//
	// 	User savedUser = userRepository.save(user);
	//
	// 	// 4. 카테고리 생성
	// 	Category category = new Category(
	// 		savedUser.getId(),
	// 		dto.getCategoryType(),
	// 		CategoryTargetType.CLUB
	// 	);
	// 	categoryRepository.save(category);
	//
	// 	// 5. 클로버 생성
	// 	CreateCloverRequestDto cloverRequest = new CreateCloverRequestDto(
	// 		0, // 초기 클로버 점수
	// 		Tier.SPROUT, // 초기 등급
	// 		savedUser.getId()
	// 	);
	// 	cloverService.createClover(cloverRequest);
	//
	// 	// 6. 회원가입 보상 지급
	// 	publisher.publishEvent(GemEvent.createEvent(
	// 		savedUser.getId(),
	// 		GemUserActivityType.SIGNUP.getGem(),
	// 		GemUserActivityType.SIGNUP.getDescription(),
	// 		LocalDateTime.now(),
	// 		null,
	// 		GemUserActivityType.SIGNUP.getFlowType(),
	// 		GemUserActivityType.SIGNUP.name()
	// 	));
	//
	// 	return SignUpUserResponseDto.from(savedUser);
	// }
	//
	// @Override
	// @Transactional
	// public SignUpUserResponseDto testSignUp(SignUpUserRequestDto dto) {
	// 	if (userRepository.existsByEmail(dto.getEmail())) {
	// 		throw new BusinessException(ResponseCode.INVALID_REQUEST, "이미 사용 중인 이메일입니다.");
	// 	}
	// 	if (userRepository.existsByPhoneNumber(dto.getPhoneNumber())) {
	// 		throw new BusinessException(ResponseCode.INVALID_REQUEST, "이미 등록된 전화번호입니다.");
	// 	}
	// 	if (!dto.getPassword().equals(dto.getConfirmPassword())) {
	// 		throw new BusinessException(ResponseCode.INVALID_REQUEST, "비밀번호와 확인이 일치하지 않습니다.");
	// 	}
	//
	// 	// 1. 비밀번호 암호화
	// 	String encodedPassword = passwordEncoder.encode(dto.getPassword());
	//
	// 	// 2. 고유한 sub 생성
	// 	String sub = UUID.randomUUID().toString();
	//
	// 	// 3. 사용자 엔티티 생성 및 저장
	// 	User user = new User(
	// 		null,
	// 		dto.getName(),
	// 		dto.getBirthday(),
	// 		dto.getEmail(),
	// 		dto.getPhoneNumber(),
	// 		UserRole.USER,
	// 		dto.getGender(),
	// 		dto.getCategoryType(),
	// 		encodedPassword,
	// 		0,
	// 		null,
	// 		false,
	// 		sub
	// 	);
	//
	// 	User savedUser = userRepository.save(user);
	//
	// 	// 4. 카테고리 생성
	// 	Category category = Category.builder()
	// 		.user(savedUser)
	// 		.categoryType(dto.getCategoryType())
	// 		.targetType(CategoryTargetType.CLUB)
	// 		.build();
	// 	categoryRepository.save(category);
	//
	// 	// 5. 클로버 생성
	// 	CreateCloverRequestDto cloverRequest = CreateCloverRequestDto.builder()
	// 		.userId(savedUser.getId())
	// 		.tier(Tier.SEED)
	// 		.build();
	// 	cloverService.createClover(cloverRequest);
	//
	// 	// 6. 회원가입 보상 지급
	// 	publisher.publishEvent(new GemEvent(savedUser.getId(), GemUserActivityType.SIGNUP));
	//
	// 	return SignUpUserResponseDto.from(savedUser);
	// }
	//
	// private void getReward(Long userId) {
	// 	publisher.publishEvent(new GemEvent(userId, GemUserActivityType.LOGIN));
	// }
}


