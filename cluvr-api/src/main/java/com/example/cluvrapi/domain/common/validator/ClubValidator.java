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

	public void validateClubCreation(CreateClubRequestDto dto) {
		if (clubRepository.existsByClubName(dto.getName())) {
			throw new BusinessException(ResponseCode.INVALID_REQUEST, "클럽명 중복");
		}
		if (dto.getName().length() > 20) {
			throw new BusinessException(ResponseCode.INVALID_REQUEST, "클럽명은 20자 이하");
		}
	}

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

	public void validateOwnerRole(ClubMemberRole role) {
		if (role != ClubMemberRole.OWNER) {
			throw new BusinessException(ResponseCode.ACCESS_DENIED, "클랜장만 가능");
		}
	}

	public void validateOwnerAndAdminRole(ClubMemberRole role) {
		if (role != ClubMemberRole.OWNER && role != ClubMemberRole.ADMIN) {
			throw new BusinessException(ResponseCode.ACCESS_DENIED, "클랜장과 운영진만 가능합니다.");
		}
	}
}
