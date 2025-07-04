package com.example.cluvrapi.global.security;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import com.example.cluvrapi.domain.clubMember.entity.enums.ClubMemberRole;
import com.example.cluvrapi.domain.clubMember.entity.enums.ClubMemberStatus;
import com.example.cluvrapi.domain.clubMember.repository.ClubMemberRepository;
import com.example.cluvrapi.domain.user.entity.User;
import com.example.cluvrapi.domain.user.repository.UserRepository;
import com.example.cluvrapi.global.exception.BusinessException;
import com.example.cluvrapi.global.response.ResponseCode;

@Component("clubSec")
@RequiredArgsConstructor
public class ClubSecurityService {

	private final ClubMemberRepository clubMemberRepository;
	private final UserRepository userRepository;

	private Long currentUserId() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth.getPrincipal() instanceof Jwt jwt)) {
			throw new BusinessException(ResponseCode.AUTH_REQUIRED);
		}

		String sub = jwt.getSubject();
		if (sub == null || sub.isBlank()) {
			throw new BusinessException(ResponseCode.INVALID_TOKEN,
				"JWT에 sub(claim)이 없습니다.");
		}

		// 레포지토리에서 직접 조회
		User user = userRepository.findBySub(sub)
			.orElseThrow(() -> new BusinessException(
				ResponseCode.USER_NOT_FOUND,
				"sub에 해당하는 사용자를 찾을 수 없습니다."
			));

		return user.getId();
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
