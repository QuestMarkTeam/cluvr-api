package com.example.cluvrapi.domain.common.validator;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import com.example.cluvrapi.domain.club.dto.request.CreateClubRequestDto;
import com.example.cluvrapi.domain.club.dto.request.UpdateClubRequestDto;
import com.example.cluvrapi.domain.club.entity.Club;
import com.example.cluvrapi.domain.club.repository.ClubRepository;
import com.example.cluvrapi.domain.clubMember.entity.enums.ClubMemberRole;
import com.example.cluvrapi.global.exception.BusinessException;
import com.example.cluvrapi.global.response.ResponseCode;

@Component
@RequiredArgsConstructor
public class ClubValidator {
	private final ClubRepository clubRepository;

	/**
	 * 클럽 생성 요청의 유효성을 검사합니다.
	 *
	 * 클럽명이 이미 존재하거나 20자를 초과하는 경우 예외를 발생시킵니다.
	 *
	 * @param dto 클럽 생성 요청 정보
	 * @throws BusinessException 클럽명이 중복되었거나 길이 제한을 초과한 경우
	 */
	public void validateClubCreation(CreateClubRequestDto dto) {
		if (clubRepository.existsByClubName(dto.getName())) {
			throw new BusinessException(ResponseCode.INVALID_REQUEST, "클럽명 중복");
		}
		if (dto.getName().length() > 20) {
			throw new BusinessException(ResponseCode.INVALID_REQUEST, "클럽명은 20자 이하");
		}
	}

	/**
	 * 클럽 정보 수정 요청 시 클럽명 변경에 대한 유효성을 검사합니다.
	 *
	 * 클럽명이 변경될 경우, 중복 여부와 20자 이하 길이 제한을 확인하며, 조건을 위반하면 BusinessException을 발생시킵니다.
	 *
	 * @param dto 클럽 정보 수정 요청 데이터
	 * @param club 현재 클럽 엔티티
	 */
	public void validateClubInfoUpdate(UpdateClubRequestDto dto, Club club) {
		if (dto.getName() != null && !dto.getName().equals(club.getName())) {
			if (clubRepository.existsByClubName(dto.getName())) {
				throw new BusinessException(ResponseCode.INVALID_REQUEST, "클럽명 중복");
			}
			if (dto.getName().length() > 20) {
				throw new BusinessException(ResponseCode.INVALID_REQUEST, "클럽명은 20자 이하");
			}
		}
	}

	/**
	 * 클럽 멤버의 역할이 OWNER인지 검증합니다.
	 *
	 * 역할이 OWNER가 아니면 ACCESS_DENIED 코드와 함께 BusinessException을 발생시킵니다.
	 */
	public void validateOwnerRole(ClubMemberRole role) {
		if (role != ClubMemberRole.OWNER) {
			throw new BusinessException(ResponseCode.ACCESS_DENIED, "클랜장만 가능");
		}
	}

	/**
	 * 클럽 멤버의 역할이 클럽장(OWNER) 또는 운영진(ADMIN)인지 검증합니다.
	 *
	 * 역할이 클럽장이나 운영진이 아닐 경우 접근이 거부되며, BusinessException이 발생합니다.
	 *
	 * @param role 검증할 클럽 멤버 역할
	 */
	public void validateOwnerAndAdminRole(ClubMemberRole role) {
		if (role != ClubMemberRole.OWNER && role != ClubMemberRole.ADMIN) {
			throw new BusinessException(ResponseCode.ACCESS_DENIED, "클랜장과 운영진만 가능합니다.");
		}
	}
}
