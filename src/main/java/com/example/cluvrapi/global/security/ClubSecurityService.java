package com.example.cluvrapi.global.security;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import com.example.cluvrapi.domain.clubMember.entity.enums.ClubMemberRole;
import com.example.cluvrapi.domain.clubMember.entity.enums.ClubMemberStatus;
import com.example.cluvrapi.domain.clubMember.repository.ClubMemberRepository;
import com.example.cluvrapi.global.exception.BusinessException;
import com.example.cluvrapi.global.response.ResponseCode;

@Component("clubSec")
@RequiredArgsConstructor
public class ClubSecurityService {

	private final ClubMemberRepository clubMemberRepository;

	private Long currentUserId() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || !(auth.getPrincipal() instanceof Jwt jwt)) {
			throw new BusinessException(
				ResponseCode.AUTH_REQUIRED
			);
		}
		Long userId = jwt.getClaim("custom:userId");

		return userId;

	}

	public boolean isMember(Long clubId) {
		var optionalMember = clubMemberRepository.findByClubIdAndUserId(clubId, currentUserId());

		boolean isActiveMember = optionalMember
			.filter(cm -> cm.getClubMemberStatus() == ClubMemberStatus.ACTIVE)
			.isPresent();

		if (!isActiveMember) {
			throw new BusinessException(
				ResponseCode.ACCESS_DENIED,
				"해당 클럽의 활성 멤버만 접근할 수 있는 기능입니다."
			);
		}

		return true;
	}

	public boolean isAdmin(Long clubId) {
		boolean admin = clubMemberRepository.findByClubIdAndUserId(clubId, currentUserId())
			.filter(cm -> cm.getClubMemberStatus() == ClubMemberStatus.ACTIVE)
			.map(cm -> {
				ClubMemberRole role = cm.getClubMemberRole();
				return role == ClubMemberRole.ADMIN || role == ClubMemberRole.OWNER;
			})
			.orElse(false);
		if (!admin) {
			throw new BusinessException(
				ResponseCode.ACCESS_DENIED,
				"클럽 관리자 또는 소유자만 접근할 수 있는 기능입니다."
			);
		}
		return true;
	}

	public boolean isOwner(Long clubId) {
		boolean owner = clubMemberRepository.findByClubIdAndUserId(clubId, currentUserId())
			.filter(cm -> cm.getClubMemberStatus() == ClubMemberStatus.ACTIVE)
			.map(cm -> cm.getClubMemberRole() == ClubMemberRole.OWNER)
			.orElse(false);
		if (!owner) {
			throw new BusinessException(
				ResponseCode.ACCESS_DENIED,
				"클럽 소유자만 접근할 수 있는 기능입니다."
			);
		}
		return true;
	}
}
