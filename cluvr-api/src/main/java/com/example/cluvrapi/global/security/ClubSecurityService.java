package com.example.cluvrapi.global.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.example.cluvrapi.domain.clubMember.entity.enums.ClubMemberRole;
import com.example.cluvrapi.domain.clubMember.entity.enums.ClubMemberStatus;
import com.example.cluvrapi.domain.clubMember.repository.ClubMemberRepository;
import com.example.cluvrapi.global.jwt.CustomUserDetails;

import lombok.RequiredArgsConstructor;

@Component("clubSec")
@RequiredArgsConstructor
public class ClubSecurityService {

	private final ClubMemberRepository clubMemberRepository;

	private Long currentUserId(){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();
		return user.getId();
	}

	public boolean isMember(Long clubId) {
		return clubMemberRepository.findByClubIdAndUserId(clubId, currentUserId())
			.filter(cm -> cm.getClubMemberStatus() == ClubMemberStatus.ACTIVE)
			.isPresent();
	}

	public boolean isAdmin(Long clubId) {
		return clubMemberRepository.findByClubIdAndUserId(clubId, currentUserId())
			.filter(cm -> cm.getClubMemberStatus() == ClubMemberStatus.ACTIVE)
			.map(cm -> {
				ClubMemberRole role = cm.getClubMemberRole();
				return role == ClubMemberRole.ADMIN || role == ClubMemberRole.OWNER;
			})
			.orElse(false);
	}

	public boolean isOwner(Long clubId) {
		return clubMemberRepository.findByClubIdAndUserId(clubId, currentUserId())
			.filter(cm -> cm.getClubMemberStatus() == ClubMemberStatus.ACTIVE)
			.map(cm -> cm.getClubMemberRole() == ClubMemberRole.OWNER)
			.orElse(false);
	}
}
