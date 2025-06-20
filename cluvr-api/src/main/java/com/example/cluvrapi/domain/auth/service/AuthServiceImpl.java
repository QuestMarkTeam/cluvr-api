package com.example.cluvrapi.domain.auth.service;

import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.cluvrapi.domain.auth.dto.request.LoginUserRequestDto;
import com.example.cluvrapi.domain.auth.dto.request.SignUpUserRequestDto;
import com.example.cluvrapi.domain.auth.dto.response.LoginUserResponseDto;
import com.example.cluvrapi.domain.auth.dto.response.SignUpUserResponseDto;
import com.example.cluvrapi.domain.auth.properties.AppProperties;
import com.example.cluvrapi.domain.category.entity.Category;
import com.example.cluvrapi.domain.category.enums.CategoryTargetType;
import com.example.cluvrapi.domain.category.repository.CategoryRepository;
import com.example.cluvrapi.domain.clover.dto.request.CreateCloverRequestDto;
import com.example.cluvrapi.domain.clover.enums.Tier;
import com.example.cluvrapi.domain.clover.service.CloverService;
import com.example.cluvrapi.domain.user.entity.User;
import com.example.cluvrapi.domain.user.entity.enums.UserRole;
import com.example.cluvrapi.domain.user.repository.UserRepository;
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

	@Override
	@Transactional
	public SignUpUserResponseDto signUp(SignUpUserRequestDto requestDto) {


		if (userRepository.existsByEmail(requestDto.getEmail())) {
			throw new BusinessException(
				ResponseCode.INVALID_REQUEST,
				"이미 사용 중인 이메일입니다."
			);
		}
		if (userRepository.existsByPhoneNumber(requestDto.getPhoneNumber())) {
			throw new BusinessException(
				ResponseCode.INVALID_REQUEST,
				"이미 등록된 전화번호입니다."
			);
		}
		if (!requestDto.getPassword().equals(requestDto.getConfirmPassword())) {
			throw new BusinessException(
				ResponseCode.INVALID_REQUEST,
				"비밀번호와 비밀번호 확인이 일치하지 않습니다."
			);
		}

		String email = requestDto.getEmail().toLowerCase();
		String domain = email.substring(email.indexOf("@") + 1);

		UserRole assignedRole = appProperties.getAdminDomains().contains(domain)
			? UserRole.ADMIN
			: UserRole.USER;

		String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

		User newUser = new User(
			null,                                    // id auto-generated
			requestDto.getName(),                    // name
			requestDto.getBirthday(),                // birthday
			email,                                   // email (소문자)
			requestDto.getPhoneNumber(),             // phoneNumber
			assignedRole,                            // UserRole: USER or ADMIN
			requestDto.getGender(),                  // gender
			requestDto.getCategoryType(),            // categoryDetail
			encodedPassword,                         // encrypted password
			0,                                       // gem 기본값
			requestDto.getImageUrl(),                // imageUrl
			false                                    // isDeleted
		);

		User savedUser = userRepository.save(newUser);

		// 6) Category, Clover 등 기존 로직 유지
		Category newCategory = new Category(
			savedUser.getId(),
			requestDto.getCategoryType(),
			CategoryTargetType.USER
		);
		categoryRepository.save(newCategory);
		cloverService.createClover(
			CreateCloverRequestDto.from(0, Tier.SPROUT, savedUser.getId())
		);

		return SignUpUserResponseDto.from(savedUser);
	}

	@Override
	@Transactional(readOnly = true)
	public LoginUserResponseDto login(LoginUserRequestDto requestDto) {

		try {
			// 1) 이메일·비밀번호 인증 시도
			Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(requestDto.getEmail(), requestDto.getPassword()));

			// 인증 성공 시 principal은 CustomUserDetails
			User user = ((CustomUserDetails)authentication.getPrincipal()).getUser();

			if (user.getIsDeleted()) {
				throw new BusinessException(
					ResponseCode.WITHDRAWN_USER_ACCESS,
					"탈퇴한 유저는 접근할 수 없습니다."
				);
			}
			// 2) 액세스 토큰 생성
			String accessToken = jwtUtil.generateAccessToken(user.getId(), user.getUserRole().name());

			// 3) 리프레시 토큰 생성 및 저장 (Redis 등)
			String refreshToken = refreshTokenService.createRefreshToken(user.getId(), user.getUserRole().name());

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
}

