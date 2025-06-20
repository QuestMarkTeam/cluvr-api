package com.example.cluvrapi.domain.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.cluvrapi.domain.user.dto.request.UpdateUserRequestDto;
import com.example.cluvrapi.domain.user.dto.response.GetUserGemResponseDto;
import com.example.cluvrapi.domain.user.dto.response.GetUserMeResponseDto;
import com.example.cluvrapi.domain.user.dto.response.GetUserOtherResponseDto;
import com.example.cluvrapi.domain.user.entity.User;
import com.example.cluvrapi.domain.user.repository.UserRepository;
import com.example.cluvrapi.global.exception.BusinessException;
import com.example.cluvrapi.global.response.ResponseCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;

	@Override
	@Transactional(readOnly = true)
	public GetUserMeResponseDto getMyProfile(Long userId) {
		User user = userRepository.findByIdNotDeleted(userId)
			.orElseThrow(() -> new BusinessException(
				ResponseCode.NOT_FOUND,
				"해당 사용자를 찾을 수 없습니다. id=" + userId
			));
		return GetUserMeResponseDto.from(user);
	}

	@Override
	@Transactional(readOnly = true)
	public GetUserOtherResponseDto getOtherUserProfile(Long otherUserId) {
		User user = userRepository.findByIdNotDeleted(otherUserId)
			.orElseThrow(() -> new BusinessException(
				ResponseCode.NOT_FOUND,
				"해당 사용자를 찾을 수 없습니다. id=" + otherUserId
			));

		return GetUserOtherResponseDto.from(user);
	}

	@Override
	@Transactional(readOnly = true)
	public GetUserGemResponseDto getUserGem(Long userId) {
		Integer gem = userRepository.findGemByIdNotDeleted(userId)
			.orElseThrow(() -> new BusinessException(
				ResponseCode.NOT_FOUND,
				"해당 사용자를 찾을 수 없습니다. id=" + userId
			));
		return new GetUserGemResponseDto(gem);
	}

	@Override
	@Transactional
	public GetUserMeResponseDto updateMyProfile(Long userId, UpdateUserRequestDto updateDto) {
		User user = userRepository.findByIdNotDeleted(userId)
			.orElseThrow(() -> new BusinessException(
				ResponseCode.NOT_FOUND,
				"해당 사용자를 찾을 수 없습니다. id=" + userId
			));

		if (updateDto.getEmail() != null && !updateDto.getEmail().isBlank()) {
			String newEmail = updateDto.getEmail();
			if (userRepository.existsByEmail(newEmail)
				&& !user.getEmail().equals(newEmail)) {
				throw new BusinessException(
					ResponseCode.INVALID_REQUEST,
					"이미 사용 중인 이메일입니다."
				);
			}
			user.changeEmail(newEmail);
		}

		if (updateDto.getPhoneNumber() != null && !updateDto.getPhoneNumber().isBlank()) {
			String newPhone = updateDto.getPhoneNumber();
			if (userRepository.existsByPhoneNumber(newPhone)
				&& !user.getPhoneNumber().equals(newPhone)) {
				throw new BusinessException(
					ResponseCode.INVALID_REQUEST,
					"이미 등록된 전화번호입니다."
				);
			}
			user.changePhoneNumber(newPhone);
		}

		if (updateDto.getCategoryType() != null) {
			user.changeCategoryType(updateDto.getCategoryType());
		}
		if (updateDto.getImageUrl() != null && !updateDto.getImageUrl().isBlank()) {
			user.changeImageUrl(updateDto.getImageUrl());
		}

		return GetUserMeResponseDto.from(user);
	}

	@Override
	@Transactional
	public void deleteMyProfile(Long userId) {
		User user = userRepository.findByIdNotDeleted(userId)
			.orElseThrow(() -> new BusinessException(
				ResponseCode.NOT_FOUND,
				"해당 사용자를 찾을 수 없습니다. id=" + userId
			));
		userRepository.delete(user);
	}
}

