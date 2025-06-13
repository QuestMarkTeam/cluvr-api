package com.example.cluvrapi.domain.clubMember.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.cluvrapi.domain.clubMember.dto.request.ChangeMemberRoleRequestDto;
import com.example.cluvrapi.domain.clubMember.dto.request.HandleJoinStatusRequestDto;
import com.example.cluvrapi.domain.clubMember.dto.response.ClubMemberInfoResponseDto;
import com.example.cluvrapi.domain.clubMember.service.ClubMemberService;
import com.example.cluvrapi.domain.common.annotation.Auth;
import com.example.cluvrapi.domain.common.dto.AuthUser;
import com.example.cluvrapi.domain.join.enums.JoinStatus;
import com.example.cluvrapi.global.response.BaseResponse;
import com.example.cluvrapi.global.response.ResponseCode;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/clubs/{clubId}")
@RequiredArgsConstructor

public class ClubMemberController {

	private final ClubMemberService clubMemberService;

	@PostMapping("/join-requests/{joinRequestId}/status")
	public ResponseEntity<Void> handleJoinStatus(
		@PathVariable Long clubId,
		@PathVariable Long joinRequestId,
		@RequestBody HandleJoinStatusRequestDto dto,
		@Auth AuthUser authUser
	) {
		JoinStatus status = dto.getStatus();
		clubMemberService.handleJoinRequest(clubId, joinRequestId, status, authUser);
		return ResponseEntity.ok().build();
	}

	@PatchMapping("/members/{memberId}/role")
	public ResponseEntity<Void> changeRole(
		@PathVariable Long clubId,
		@PathVariable("memberId") Long targetMemberId,
		@RequestBody @Valid ChangeMemberRoleRequestDto dto,
		@Auth AuthUser authUser
	) {
		clubMemberService.changeMemberRole(clubId, authUser, targetMemberId, dto.getNewRole());
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/members/me")
	public ResponseEntity<Void> withdrawFromClub(
		@PathVariable Long clubId,
		@Auth AuthUser authUser
	) {
		clubMemberService.withdrawFromClub(clubId, authUser);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/members/{memberId}")
	public ResponseEntity<Void> kickMember(
		@PathVariable Long clubId,
		@PathVariable Long memberId,
		@Auth AuthUser authUser
	) {
		clubMemberService.kickMember(clubId, authUser, memberId);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/members")
	public ResponseEntity<BaseResponse<Page<ClubMemberInfoResponseDto>>> listMembers(
		@PathVariable Long clubId,
		@Auth AuthUser authUser,
		@PageableDefault(size = 20) Pageable pageable
	) {
		Page<ClubMemberInfoResponseDto> page = clubMemberService.listMembers(clubId, authUser, pageable);
		return ResponseEntity
			.ok(BaseResponse.success(page, ResponseCode.OK));
	}

}
