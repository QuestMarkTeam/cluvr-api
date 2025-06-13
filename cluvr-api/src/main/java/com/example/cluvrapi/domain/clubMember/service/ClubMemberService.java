package com.example.cluvrapi.domain.clubMember.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.cluvrapi.domain.clubMember.dto.response.ClubMemberInfoResponseDto;
import com.example.cluvrapi.domain.clubMember.dto.response.GetMemberRoleResponseDto;
import com.example.cluvrapi.domain.clubMember.entity.enums.ClubMemberRole;
import com.example.cluvrapi.domain.common.dto.AuthUser;
import com.example.cluvrapi.domain.join.enums.JoinStatus;

import jakarta.validation.constraints.NotNull;

public interface ClubMemberService {
	void handleJoinRequest(Long clubId, Long joinRequestId, JoinStatus status, AuthUser approver);

	void changeMemberRole(Long clubId, AuthUser operator, Long targetUserId,
		@NotNull(message = "변경할 역할을 지정하세요.") ClubMemberRole newRole);

	void withdrawFromClub(Long clubId, AuthUser user);

	void kickMember(Long clubId, AuthUser operator, Long targetMemberId);

	Page<ClubMemberInfoResponseDto> listMembers(Long clubId, AuthUser authUser, Pageable pageable);

	GetMemberRoleResponseDto getMemberRole(Long clubId, Long targetUserId, AuthUser requester);
}
