package com.example.cluvrapi.domain.user.service;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.cluvrapi.domain.category.entity.Category;
import com.example.cluvrapi.domain.category.enums.CategoryTargetType;
import com.example.cluvrapi.domain.category.repository.CategoryRepository;
import com.example.cluvrapi.domain.user.dto.request.LoginUserRequestDto;
import com.example.cluvrapi.domain.user.dto.request.SignUpUserRequestDto;
import com.example.cluvrapi.domain.user.dto.response.GetUserMeResponseDto;
import com.example.cluvrapi.domain.user.dto.response.GetUserOtherResponseDto;
import com.example.cluvrapi.domain.user.dto.response.GetUserPointResponseDto;
import com.example.cluvrapi.domain.user.dto.response.LoginUserResponseDto;
import com.example.cluvrapi.domain.user.dto.response.SignUpUserResponseDto;
import com.example.cluvrapi.domain.user.entity.User;
import com.example.cluvrapi.domain.user.entity.enums.UserRole;
import com.example.cluvrapi.domain.user.repository.UserRepository;
import com.example.cluvrapi.global.jwt.CustomUserDetails;
import com.example.cluvrapi.global.jwt.JwtUtil;
import com.example.cluvrapi.global.jwt.RefreshTokenServiceImpl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepository;
	private final AuthenticationManager authenticationManager;
	private final JwtUtil jwtUtil;
	private final RefreshTokenServiceImpl refreshTokenService;
	private final CategoryRepository categoryRepository;

	@Override
	@Transactional
	public SignUpUserResponseDto signUp(SignUpUserRequestDto requestDto) {
		if (!requestDto.getPassword().equals(requestDto.getConfirmPassword())) {
			throw new IllegalArgumentException("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
		}

		String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

		User newUser = new User(null,                                      // id → 데이터베이스에서 자동 생성
			requestDto.getName(),                      // name
			requestDto.getBirthday(),                  // birthday
			requestDto.getEmail(),                     // email
			requestDto.getPhoneNumber(),               // phoneNumber
			UserRole.USER,                             // 가입 시 기본 권한(예: USER)
			requestDto.getGender(),                    // gender
			requestDto.getCategoryType(),            // categoryDetail
			encodedPassword,                           // 암호화된 password
			0L,                                        // point 기본값
			requestDto.getImageUrl(),                  // imageUrl (null 허용될 경우 DTO에서 null 가능)
			false                                      // isDeleted: 신규 가입이므로 false
		);

		User savedUser = userRepository.save(newUser);

		// 5) Category 엔티티 생성 및 저장 (유저용)
		Category newCategory = new Category(savedUser.getId(),                // targetId = 유저 ID
			requestDto.getCategoryType(),     // CategoryType (예: DEVELOPMENT, MUSIC_EDUCATION 등)
			CategoryTargetType.USER           // targetType = USER
		);
		categoryRepository.save(newCategory);

		return SignUpUserResponseDto.from(savedUser);
	}

	@Override
	public LoginUserResponseDto login(LoginUserRequestDto requestDto) {
		// 1) 이메일·비밀번호 인증 시도
		Authentication authentication = authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(requestDto.getEmail(), requestDto.getPassword()));

		// 인증 성공 시 principal은 CustomUserDetails
		User user = ((CustomUserDetails)authentication.getPrincipal()).getUser();

		// 2) 액세스 토큰 생성
		String accessToken = jwtUtil.generateAccessToken(user.getId(), user.getUserRole().name());

		// 3) 리프레시 토큰 생성 및 저장 (Redis 등)
		String refreshToken = refreshTokenService.createRefreshToken(user.getId(), user.getUserRole().name());

		// 4) DTO 변환 후 반환
		return LoginUserResponseDto.from(user, accessToken, refreshToken);
	}

	@Override
	@Transactional(readOnly = true)
	public GetUserMeResponseDto getMyProfile(Long userId) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new NoSuchElementException("해당 사용자를 찾을 수 없습니다. id=" + userId));
		return GetUserMeResponseDto.from(user);
	}

	@Override
	@Transactional(readOnly = true)
	public GetUserOtherResponseDto getOtherUserProfile(Long otherUserId) {
		User user = userRepository.findById(otherUserId)
			.orElseThrow(() -> new NoSuchElementException("해당 사용자를 찾을 수 없습니다. id=" + otherUserId));

		return GetUserOtherResponseDto.from(user);
	}

	@Override
	@Transactional(readOnly = true)
	public GetUserPointResponseDto getUserPoint(Long userId) {
		Optional<Long> optPoint = userRepository.findPointById(userId);

		if (optPoint.isEmpty()) {
			throw new IllegalArgumentException("해당 ID의 사용자를 찾을 수 없습니다. id=" + userId);
		}

		Long point = optPoint.get();
		return new GetUserPointResponseDto(point);
	}
}

