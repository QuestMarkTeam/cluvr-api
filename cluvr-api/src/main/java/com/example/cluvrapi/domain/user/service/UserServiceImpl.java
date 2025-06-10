package com.example.cluvrapi.domain.user.service;

import java.util.NoSuchElementException;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.cluvrapi.domain.user.dto.request.UpdateUserRequestDto;
import com.example.cluvrapi.domain.user.dto.response.GetUserMeResponseDto;
import com.example.cluvrapi.domain.user.dto.response.GetUserOtherResponseDto;
import com.example.cluvrapi.domain.user.dto.response.GetUserPointResponseDto;
import com.example.cluvrapi.domain.user.entity.User;
import com.example.cluvrapi.domain.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;

	@Override
	@Transactional(readOnly = true)
	public GetUserMeResponseDto getMyProfile(Long userId) {
		User user = userRepository.findByIdNotDeleted(userId)
			.orElseThrow(() -> new NoSuchElementException("해당 사용자를 찾을 수 없습니다. id=" + userId));
		return GetUserMeResponseDto.from(user);
	}

	@Override
	@Transactional(readOnly = true)
	public GetUserOtherResponseDto getOtherUserProfile(Long otherUserId) {
		User user = userRepository.findByIdNotDeleted(otherUserId)
			.orElseThrow(() -> new NoSuchElementException("해당 사용자를 찾을 수 없습니다. id=" + otherUserId));

		return GetUserOtherResponseDto.from(user);
	}

	@Override
	@Transactional(readOnly = true)
	public GetUserPointResponseDto getUserPoint(Long userId) {
		Optional<Integer> optGem = userRepository.findPointByIdNotDeleted(userId);

		if (optGem.isEmpty()) {
			throw new IllegalArgumentException("해당 ID의 사용자를 찾을 수 없습니다. id=" + userId);
		}

		Integer gem = optGem.get();
		return new GetUserPointResponseDto(gem);
	}

	@Override
	public GetUserMeResponseDto updateMyProfile(Long userId, UpdateUserRequestDto updateDto) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException("해당 ID의 사용자를 찾을 수 없습니다. id=" + userId));

		if (updateDto.getEmail() != null && !updateDto.getEmail().isBlank()) {
			user.changeEmail(updateDto.getEmail());
		}

		if (updateDto.getPhoneNumber() != null && !updateDto.getPhoneNumber().isBlank()) {
			user.changePhoneNumber(updateDto.getPhoneNumber());
		}

		if (updateDto.getCategoryType() != null) {
			user.changeCategoryType(updateDto.getCategoryType());
		}

		if (updateDto.getImageUrl() != null && !updateDto.getImageUrl().isBlank()) {
			user.changeImageUrl(updateDto.getImageUrl());
		}

		userRepository.save(user);

		return GetUserMeResponseDto.from(user);

		String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

		User newUser = new User(null,                                      // id → 데이터베이스에서 자동 생성
			requestDto.getName(),                      // name
			requestDto.getBirthday(),                  // birthday
			requestDto.getEmail(),                     // email
			requestDto.getPhoneNumber(),               // phoneNumber
			UserRole.USER,                             // 가입 시 기본 권한(예: USER)
			requestDto.getGender(),                    // gender
			requestDto.getCategoryDetail(),            // categoryDetail
			encodedPassword,                           // 암호화된 password
			0,                                        // point 기본값
			requestDto.getImageUrl(),                  // imageUrl (null 허용될 경우 DTO에서 null 가능)
			false                                      // isDeleted: 신규 가입이므로 false
		);

		User savedUser = userRepository.save(newUser);

		return SignUpUserResponseDto.from(savedUser);
	}

	@Override
	@Transactional
	public void deleteMyProfile(Long userId) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new NoSuchElementException("해당 사용자를 찾을 수 없습니다. id=" + userId));
		userRepository.delete(user);
	}
}

