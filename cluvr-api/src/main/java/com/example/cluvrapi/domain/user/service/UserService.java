package com.example.cluvrapi.domain.user.service;

import com.example.cluvrapi.domain.user.dto.request.UpdateUserRequestDto;
import com.example.cluvrapi.domain.user.dto.response.GetUserGemResponseDto;
import com.example.cluvrapi.domain.user.dto.response.GetUserMeResponseDto;
import com.example.cluvrapi.domain.user.dto.response.GetUserOtherResponseDto;

/**
 * 설명: 사용자 프로필 조회, 수정, 삭제 및 보석(gem) 조회 기능을 제공하는 서비스 인터페이스입니다.
 *
 * <p>회원 정보 조회 및 관리에 관련된 핵심 비즈니스 로직을 정의합니다.
 *
 * @author 정승원
 */
public interface UserService {

	/**
	 * 설명: 인증된 사용자의 나의 프로필 정보를 조회합니다.
	 *
	 * <p>사용자 ID를 기반으로 닉네임, 이메일 등 개인 정보를 포함한 프로필을 반환합니다.
	 *
	 * @param userId 설명: 프로필을 조회할 사용자 식별자
	 * @return GetUserMeResponseDto 설명: 사용자 개인 프로필 정보 DTO
	 * @throws BusinessException 설명: 사용자를 찾을 수 없거나 접근 권한이 없을 경우 발생
	 */
	GetUserMeResponseDto getMyProfile(Long userId);

	/**
	 * 설명: 다른 사용자의 프로필 정보를 조회합니다.
	 *
	 * <p>다른 사용자 ID를 기반으로 공개 프로필 정보를 반환합니다.
	 *
	 * @param otherUserId 설명: 조회할 다른 사용자 식별자
	 * @return GetUserOtherResponseDto 설명: 다른 사용자 프로필 정보 DTO
	 * @throws BusinessException 설명: 해당 사용자를 찾을 수 없거나 접근 권한이 없을 경우 발생
	 */
	GetUserOtherResponseDto getOtherUserProfile(Long otherUserId);

	/**
	 * 설명: 사용자의 보석(gem) 보유량을 조회합니다.
	 *
	 * <p>사용자 ID를 기반으로 현재 보유한 보석 수량 정보를 반환합니다.
	 *
	 * @param userId 설명: 보석 정보를 조회할 사용자 식별자
	 * @return GetUserGemResponseDto 설명: 사용자의 보석 보유 정보 DTO
	 * @throws BusinessException 설명: 사용자를 찾을 수 없을 경우 발생
	 */
	GetUserGemResponseDto getUserGem(Long userId);

	/**
	 * 설명: 인증된 사용자의 프로필 정보를 수정합니다.
	 *
	 * <p>업데이트 DTO에 포함된 필드(이름, 프로필 이미지 등)를 적용하여 프로필을 변경하고,
	 *       변경된 프로필 정보를 반환합니다.
	 *
	 * @param userId    설명: 수정 대상 사용자의 식별자
	 * @param updateDto 설명: 수정할 프로필 정보가 담긴 DTO
	 * @return GetUserMeResponseDto 설명: 수정 후 사용자 개인 프로필 정보 DTO
	 * @throws BusinessException 설명: 사용자를 찾을 수 없거나 검증 실패 시 발생
	 */
	GetUserMeResponseDto updateMyProfile(Long userId, UpdateUserRequestDto updateDto);

	/**
	 * 설명: 인증된 사용자의 계정을 탈퇴(삭제) 처리합니다.
	 *
	 * <p>해당 사용자의 프로필과 관련 데이터를 소프트 삭제 처리하거나
	 *       완전 삭제 후 재가입이 불가능하도록 설정합니다.
	 *
	 * @param userId 설명: 삭제할 사용자 식별자
	 * @throws BusinessException 설명: 사용자를 찾을 수 없거나 삭제 처리에 실패했을 경우 발생
	 */
	void deleteMyProfile(Long userId);

}
