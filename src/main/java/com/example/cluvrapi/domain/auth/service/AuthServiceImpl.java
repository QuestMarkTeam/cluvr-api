package com.example.cluvrapi.domain.auth.service;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
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
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
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
import com.example.cluvrapi.domain.user.entity.User;
import com.example.cluvrapi.domain.user.entity.enums.Gender;
import com.example.cluvrapi.domain.user.entity.enums.UserRole;
import com.example.cluvrapi.domain.user.repository.UserRepository;
import com.example.cluvrapi.global.exception.BusinessException;
import com.example.cluvrapi.global.response.ResponseCode;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminConfirmSignUpRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminUpdateUserAttributesRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AttributeType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AuthFlowType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AuthenticationResultType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.CognitoIdentityProviderException;
import software.amazon.awssdk.services.cognitoidentityprovider.model.InitiateAuthRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.InvalidParameterException;
import software.amazon.awssdk.services.cognitoidentityprovider.model.NotAuthorizedException;
import software.amazon.awssdk.services.cognitoidentityprovider.model.RevokeTokenRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.SignUpRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.UsernameExistsException;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepository;
	private final CategoryRepository categoryRepository;
	private final JwtDecoder jwtDecoder;
	private final CloverService cloverService;
	private final AppProperties appProperties;
	private final RedisTemplate<String, Object> redisTemplate;
	private final SecureRandom random = new SecureRandom();
	private final ApplicationEventPublisher publisher; // 이벤트 발행
	private final GemService gemService;

	private final JavaMailSender mailSender;

	private static final Duration VERIFY_TTL = Duration.ofMinutes(10);

	@Qualifier("cognitoUserClient")
	private final CognitoIdentityProviderClient cognitoUserClient;

	@Qualifier("cognitoAdminClient")
	private final CognitoIdentityProviderClient cognitoAdminClient;
	private final RestTemplate restTemplate;

	@Value("${cognito.token-endpoint}")
	private String tokenEndpoint;

	@Value("${cognito.redirect-uri}")
	private String redirectUri;

	@Value("${spring.mail.username}")
	private String mailFrom;

	@Value("${cognito.userPoolId}")
	private String userPoolId;

	@Value("${cognito.clientId}")
	private String clientId;

	@Value("${cognito.clientSecret}")
	private String clientSecret;

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
		msg.setFrom(mailFrom);
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
			// 1. cognito에 전달할 로그인 파라미터 설정 (USERNAME/PASSWORD 방식)
			Map<String, String> authParams = Map.of(
				"USERNAME", requestDto.getEmail(),
				"PASSWORD", requestDto.getPassword(),
				"SECRET_HASH", calculateSecretHash(clientId, clientSecret, requestDto.getEmail())
			);

			// 2. Cognito 인증 요청
			InitiateAuthRequest authRequest = InitiateAuthRequest.builder()
				.authFlow(AuthFlowType.USER_PASSWORD_AUTH)
				.clientId(clientId)
				.authParameters(authParams)
				.build();

			// 3. Cognito에 로그인 요청 -> accessToken, idToken, refreshToken 반환
			AuthenticationResultType result = cognitoUserClient
				.initiateAuth(authRequest)
				.authenticationResult();
			// 4. 반환된 idToken을 Spring의 JwtDecoder로 디코딩해서 사용자 정보 추출
			Jwt idToken = jwtDecoder.decode(result.idToken());
			String sub = idToken.getSubject();
			String email = idToken.getClaim("email");   // email 클레임 (Cognito 설정에 따라 포함돼야 함)
			Boolean emailVerified = idToken.getClaim("email_verified");     // 이름 클레임 (없을 수도 있음)

			// 3. 이메일 인증 여부 확인
			if (emailVerified == null || !emailVerified) {
				throw new BusinessException(ResponseCode.EMAIL_NOT_VERIFIED);
			}

			// 4. 내부 DB에서 사용자 조회
			User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new BusinessException(ResponseCode.USER_NOT_FOUND));

			// 5. 탈퇴 사용자 확인
			if (user.getIsDeleted()) {
				throw new BusinessException(
					ResponseCode.WITHDRAWN_USER_ACCESS,
					"탈퇴한 유저는 접근할 수 없습니다."
				);
			}

			Long userId = user.getId();
			//
			// // 3) 리프레시 토큰 생성 및 저장 (Redis 등)
			// String refreshToken = refreshTokenService.createRefreshToken(userId, user.getUserRole().name());

			// 3.5) Gem 적립
			getReward(userId);

			// 4) DTO 변환 후 반환
			return LoginUserResponseDto.from(
				email,
				// idToken.getClaim("name"), // name 클레임이 없으면 null 처리됨
				user.getName(),
				result.accessToken(),
				result.refreshToken(),
				result.idToken()
			);

		} catch (BadCredentialsException ex) {
			throw new BusinessException(ResponseCode.LOGIN_FAILED);
		} catch (NotAuthorizedException e) {
			log.warn("Cognito 인증 실패 - 이메일 또는 비밀번호 불일치: {}", e.awsErrorDetails().errorMessage(), e);
			throw new BusinessException(ResponseCode.LOGIN_FAILED);
		} catch (InvalidParameterException e) {
			log.warn("Cognito 파라미터 에러: {}", e.awsErrorDetails().errorMessage(), e);
			throw new BusinessException(ResponseCode.LOGIN_FAILED);
		} catch (Exception e) {
			log.error("Cognito 로그인 중 예외 발생", e);
			throw new BusinessException(ResponseCode.INTERNAL_ERROR); // 예외 케이스에 따라 다르게 처리해도 됨
		}
	}

	//accessToken 의 삭제를 프론트 측에서하고, 백엔드는 cognito의 호출을 revoke해주는 정도만 해줘도됨!
	@Override
	public void logout(String refreshToken) {

		if (refreshToken == null || refreshToken.isBlank()) {
			throw new BusinessException(
				ResponseCode.AUTH_REQUIRED,
				"로그아웃할 토큰이 제공되지 않았습니다."
			);
		}
		RevokeTokenRequest revokeTokenRequest = RevokeTokenRequest.builder()
			.clientId(clientId)
			.clientSecret(clientSecret)
			.token(refreshToken)
			.build();

		try {
			cognitoUserClient.revokeToken(revokeTokenRequest);
			log.info(" Refresh token 사라짐");
		} catch (CognitoIdentityProviderException e) {
			log.error(" Cognito 에러 발생: {}", e.awsErrorDetails().errorMessage());
			throw e;
		} catch (Exception e) {
			log.error(" 일반 예외 발생", e);
			throw e;
		}
	}

	private String calculateSecretHash(String clientId, String clientSecret, String username) {
		try {
			String message = username + clientId;
			Mac mac = Mac.getInstance("HmacSHA256");
			mac.init(new SecretKeySpec(clientSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
			byte[] hmac = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));
			return Base64.getEncoder().encodeToString(hmac);
		} catch (Exception e) {
			throw new RuntimeException("SECRET_HASH 생성 실패", e);
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

		log.info("📨 [VERIFY] 요청 이메일: {}", req.getEmail());
		log.info("📨 [VERIFY] 캐시에서 꺼낸 이메일: {}", cache.getSignUpRequest().getEmail());
		log.info("📨 [VERIFY] 캐시에서 꺼낸 코드: {}", cache.getVerificationCode());
		log.info("📨 [VERIFY] 입력된 코드: {}", req.getCode());

		SignUpUserRequestDto dto = cache.getSignUpRequest();

		log.info("clientId: [{}]", clientId);
		log.info("clientSecret (base64): [{}]",
			Base64.getEncoder().encodeToString(clientSecret.getBytes(StandardCharsets.UTF_8)));
		log.info("username={}", dto.getEmail());
		log.info("clientId={}", clientId);
		log.info("clientSecret(raw)={}", clientSecret);
		log.info("secretHash={}", calculateSecretHash(clientId, clientSecret, dto.getEmail()));

		//  Cognito에 등록
		try {
			String username = dto.getEmail(); // 이메일을 곧 username으로 씀
			SignUpRequest signUpRequest = SignUpRequest.builder()
				.clientId(clientId)
				.username(username)
				.password(dto.getPassword())
				.secretHash(calculateSecretHash(clientId, clientSecret, username))
				.userAttributes(List.of(
					AttributeType.builder().name("email").value(dto.getEmail()).build(),
					AttributeType.builder().name("name").value(dto.getName()).build()
				))
				.build();
			cognitoUserClient.signUp(signUpRequest);

			log.info("🔑 SecretHash 생성용 username: {}, hash: {}", username,
				calculateSecretHash(clientId, clientSecret, username));

			AdminConfirmSignUpRequest confirmRequest = AdminConfirmSignUpRequest.builder()
				.userPoolId(userPoolId)
				.username(username)
				.build();
			cognitoAdminClient.adminConfirmSignUp(confirmRequest);

			AdminUpdateUserAttributesRequest updateRequest = AdminUpdateUserAttributesRequest.builder()
				.userPoolId(userPoolId)
				.username(username)
				.userAttributes(AttributeType.builder()
					.name("email_verified")
					.value("true")
					.build())
				.build();
			cognitoAdminClient.adminUpdateUserAttributes(updateRequest);

		} catch (UsernameExistsException e) {
			throw new BusinessException(ResponseCode.ALREADY_COGNITO);
		} catch (CognitoIdentityProviderException e) {
			log.error("Cognito 가입 실패: {}", e.awsErrorDetails().errorMessage(), e);
			throw new BusinessException(ResponseCode.INTERNAL_ERROR);
		}

		AuthenticationResultType authResult = cognitoAdminClient
			.adminInitiateAuth(builder -> builder
				.authFlow(AuthFlowType.ADMIN_NO_SRP_AUTH)
				.userPoolId(userPoolId)
				.clientId(clientId)
				.authParameters(Map.of(
					"USERNAME", dto.getEmail(),
					"PASSWORD", dto.getPassword(),
					"SECRET_HASH", calculateSecretHash(clientId, clientSecret, dto.getEmail())
				))
			).authenticationResult();

		log.info("SignUp username: [{}]", dto.getEmail());
		log.info("SignUp secretHash: [{}]", calculateSecretHash(clientId, clientSecret, dto.getEmail()));

		Jwt jwt = jwtDecoder.decode(authResult.idToken());
		String sub = jwt.getSubject();

		// 로컬 DB 저장
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
			false,
			sub
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

	@Override
	@Transactional
	public SignUpUserResponseDto testSignUp(SignUpUserRequestDto dto) {

		String emailLower = dto.getEmail().toLowerCase();

		if (userRepository.existsByEmail(emailLower)) {
			throw new BusinessException(ResponseCode.INVALID_REQUEST, "이미 사용 중인 이메일입니다.");
		}
		if (userRepository.existsByPhoneNumber(dto.getPhoneNumber())) {
			throw new BusinessException(ResponseCode.INVALID_REQUEST, "이미 등록된 전화번호입니다.");
		}
		if (!dto.getPassword().equals(dto.getConfirmPassword())) {
			throw new BusinessException(ResponseCode.INVALID_REQUEST, "비밀번호와 확인이 일치하지 않습니다.");
		}

		//바로 cognito 가입 & 확인
		try {
			String username = dto.getEmail();
			cognitoUserClient.signUp(SignUpRequest.builder()
				.clientId(clientId)
				.username(username)
				.password(dto.getPassword())
				.secretHash(calculateSecretHash(clientId, clientSecret, username))
				.userAttributes(List.of(
					AttributeType.builder().name("email").value(dto.getEmail()).build(),
					AttributeType.builder().name("name").value(dto.getName()).build()
				))
				.build());

			cognitoAdminClient.adminConfirmSignUp(AdminConfirmSignUpRequest.builder()
				.userPoolId(userPoolId)
				.username(username)
				.build());

			cognitoAdminClient.adminUpdateUserAttributes(AdminUpdateUserAttributesRequest.builder()
				.userPoolId(userPoolId)
				.username(username)
				.userAttributes(AttributeType.builder()
					.name("email_verified")
					.value("true")
					.build())
				.build());
		} catch (UsernameExistsException e) {
			throw new BusinessException(ResponseCode.ALREADY_COGNITO);
		} catch (CognitoIdentityProviderException e) {
			log.error("cognito 등록이 안됨 사유 : {}", e.awsErrorDetails().errorMessage(), e);
			throw new BusinessException(ResponseCode.INTERNAL_ERROR);
		}

		AuthenticationResultType authResult = cognitoUserClient
			.adminInitiateAuth(builder -> builder
				.authFlow(AuthFlowType.ADMIN_NO_SRP_AUTH)
				.userPoolId(userPoolId)
				.clientId(clientId)
				.authParameters(Map.of(
					"USERNAME", dto.getEmail(),
					"PASSWORD", dto.getPassword(),
					"SECRET_HASH", calculateSecretHash(clientId, clientSecret, dto.getEmail())
				))
			).authenticationResult();

		Jwt jwt = jwtDecoder.decode(authResult.idToken());
		String sub = jwt.getSubject();

		// 로컬 DB 저장
		String domain = emailLower.substring(emailLower.indexOf('@') + 1);
		UserRole role = appProperties.getAdminDomains().contains(domain)
			? UserRole.ADMIN : UserRole.USER;

		User user = new User(
			null,                        // id (자동 생성)
			dto.getName(),
			dto.getBirthday(),
			emailLower,
			dto.getPhoneNumber(),
			role,            // 기본 USER 권한
			dto.getGender(),
			dto.getCategoryType(),
			passwordEncoder.encode(dto.getPassword()),
			0,                           // 초기 포인트 등
			dto.getImageUrl(),
			false,                        // isDeleted=false
			sub
		);

		User saved = userRepository.save(user);

		categoryRepository.save(new Category(
			saved.getId(),
			dto.getCategoryType(),
			CategoryTargetType.USER
		));

		// 5) 클로버 생성
		cloverService.createClover(
			CreateCloverRequestDto.from(0, Tier.SPROUT, saved.getId())
		);

		return SignUpUserResponseDto.from(saved);
	}


	@Override
	@Transactional
	public SocialLoginResponseDto socialLogin(String code) {
		// 1) Authorization Code → Token 교환
		HttpHeaders headers = new HttpHeaders();
		headers.setBasicAuth(clientId, clientSecret);
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
		form.add("grant_type", "authorization_code");
		form.add("client_id", clientId);
		form.add("code", code);
		form.add("redirect_uri", redirectUri);

		HttpEntity<MultiValueMap<String, String>> requestEntity =
			new HttpEntity<>(form, headers);

		JsonNode tokenResp = restTemplate.postForObject(
			tokenEndpoint,
			requestEntity,
			JsonNode.class
		);

		String accessToken  = tokenResp.get("access_token").asText();
		String refreshToken = tokenResp.get("refresh_token").asText();
		String idToken      = tokenResp.get("id_token").asText();

		// 2) ID 토큰 디코딩 → 사용자 정보 추출
		Jwt jwt      = jwtDecoder.decode(idToken);
		String sub   = jwt.getSubject();
		String email = jwt.getClaim("email");
		String name  = jwt.getClaim("name");
		String emailLower = email.toLowerCase();

		String defaultName = emailLower.substring(0, emailLower.indexOf('@'));


		// 3) 기존 사용자 조회, 없으면 기본값으로 가입
		User user = userRepository.findBySubAndNotDeleted(sub)
			.orElseGet(() -> persistSocialLocalUser(sub, defaultName, emailLower));

		// 4) 빈 missingFields와 함께 반환
		return SocialLoginResponseDto.of(
			accessToken,
			refreshToken,
			idToken,
			List.of()
		);
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

	private User persistSocialLocalUser(
		String sub,
		String name,
		String email
	) {
		// 역할 결정
		String domain = email.substring(email.indexOf('@') + 1);
		UserRole role = appProperties.getAdminDomains().contains(domain)
			? UserRole.ADMIN
			: UserRole.USER;

		// 랜덤 패스워드 생성 (실제 사용자는 사용하지 않음)
		String rawPassword = UUID.randomUUID().toString();
		String encodedPassword = passwordEncoder.encode(rawPassword);

		// User 엔티티 생성 (gender=MALE, categoryType=DEVELOPMENT 고정)
		User user = new User(
			null,                       // id (auto-generated)
			name,                       // name
			LocalDate.of(1970,1,1),     // 기본 birthday
			email,                      // email
			null,                         // 기본 phoneNumber
			role,                       // userRole
			Gender.MALE,                // gender 고정: MALE
			CategoryType.DEVELOPMENT,   // categoryType 고정: DEVELOPMENT
			encodedPassword,            // password
			0,                          // 초기 gem 포인트
			"",                         // imageUrl 기본값
			false,                      // isDeleted=false
			sub                         // Cognito sub
		);
		User saved = userRepository.save(user);

		// Category 생성
		categoryRepository.save(new Category(
			saved.getId(),
			saved.getCategoryType(),
			CategoryTargetType.USER
		));

		// Clover 초기화
		cloverService.createClover(
			CreateCloverRequestDto.from(0, Tier.SPROUT, saved.getId())
		);

		return saved;
	}

}


