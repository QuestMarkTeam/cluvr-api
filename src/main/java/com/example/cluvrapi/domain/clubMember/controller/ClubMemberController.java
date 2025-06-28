package com.example.cluvrapi.domain.clubMember.controller;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

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
import com.example.cluvrapi.domain.clubMember.dto.response.GetMemberRoleResponseDto;
import com.example.cluvrapi.domain.clubMember.service.ClubMemberService;
import com.example.cluvrapi.domain.common.annotation.Auth;
import com.example.cluvrapi.domain.common.dto.AuthUser;
import com.example.cluvrapi.global.response.BaseResponse;
import com.example.cluvrapi.global.response.ResponseCode;

/**
 * 클럽 멤버 관련 요청을 처리하는 컨트롤러입니다.
 */

@RestController
@RequestMapping("/api/clubs/{clubId}")
@RequiredArgsConstructor
public class ClubMemberController {

	private final ClubMemberService clubMemberService;

	/**
	 * 가입 요청 승인 또는 거절 처리
	 *
	 * @param clubId 클럽 ID
	 * @param joinRequestId 가입 요청 ID
	 * @param dto 요청 상태 정보 (ACCEPTED / REJECTED 등)
	 * @param authUser 인증된 사용자 정보
	 * @author alpomjeong
	 */

	@PostMapping("/join-requests/{joinRequestId}/status")
	public ResponseEntity<BaseResponse<Void>> handleJoinStatus(
		@PathVariable Long clubId,
		@PathVariable Long joinRequestId,
		@RequestBody HandleJoinStatusRequestDto dto,
		@Auth AuthUser authUser
	) {
		clubMemberService.handleJoinRequest(clubId, joinRequestId, dto.getStatus(), authUser);
		return ResponseEntity
			.ok(BaseResponse.success(ResponseCode.OK));
	}

	/**
	 * 특정 멤버의 클럽 내 역할(Role) 변경
	 *
	 * @param clubId 클럽 ID
	 * @param targetMemberId 역할을 변경할 대상 멤버 ID
	 * @param dto 새로운 역할 정보
	 * @param authUser 인증된 사용자 정보
	 * @author alpomjeong
	 */

	@PatchMapping("/members/{memberId}/role")
	public ResponseEntity<BaseResponse<Void>> changeRole(
		@PathVariable Long clubId,
		@PathVariable("memberId") Long targetMemberId,
		@RequestBody @Valid ChangeMemberRoleRequestDto dto,
		@Auth AuthUser authUser
	) {
		clubMemberService.changeMemberRole(clubId, authUser, targetMemberId, dto.getNewRole());
		return ResponseEntity
			.ok(BaseResponse.success(ResponseCode.OK));
	}

	/**
	 * 현재 로그인한 사용자의 클럽 탈퇴 처리
	 *
	 * @param clubId 클럽 ID
	 * @param authUser 인증된 사용자 정보
	 * @author alpomjeong
	 */

	@DeleteMapping("/members/me")
	public ResponseEntity<BaseResponse<String>> withdrawFromClub(
		@PathVariable Long clubId,
		@Auth AuthUser authUser
	) {
		clubMemberService.withdrawFromClub(clubId, authUser);
		return ResponseEntity
			.ok(
				BaseResponse.success(
					"클럽 탈퇴가 완료되었습니다.",
					ResponseCode.OK
				)
			);
	}

	/**
	 * 특정 멤버를 클럽에서 강퇴
	 *
	 * @param clubId 클럽 ID
	 * @param memberId 강퇴 대상 멤버 ID
	 * @param authUser 인증된 사용자 정보
	 * @author alpomjeong
	 */

	@DeleteMapping("/members/{memberId}")
	public ResponseEntity<BaseResponse<String>> kickMember(
		@PathVariable Long clubId,
		@PathVariable Long memberId,
		@Auth AuthUser authUser
	) {
		clubMemberService.kickMember(clubId, authUser, memberId);
		return ResponseEntity
			.ok(
				BaseResponse.success(
					"멤버 강퇴가 완료되었습니다.",
					ResponseCode.OK
				)
			);
	}

	/**
	 * 클럽 멤버 목록 조회 (페이징 처리)
	 *
	 * @param clubId 클럽 ID
	 * @param authUser 인증된 사용자 정보
	 * @param pageable 페이징 정보
	 * @author alpomjeong
	 */

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

	/**
	 * 특정 유저의 클럽 내 역할(Role) 조회
	 *
	 * @param clubId 클럽 ID
	 * @param targetUserId 조회 대상 유저 ID
	 * @param authUser 인증된 사용자 정보
	 * @author alpomjeong
	 */

	@GetMapping("/members/{userId}/role")
	public ResponseEntity<BaseResponse<GetMemberRoleResponseDto>> getMemberRole(
		@PathVariable Long clubId,
		@PathVariable("userId") Long targetUserId,
		@Auth AuthUser authUser
	) {
		GetMemberRoleResponseDto dto = clubMemberService.getMemberRole(clubId, targetUserId);
		return ResponseEntity
			.ok(BaseResponse.success(dto, ResponseCode.OK));
	}

	/**
	 * 클럽장 권한을 다른 멤버에게 이양
	 *
	 * @param clubId 클럽 ID
	 * @param targetMemberId 새로운 클럽장으로 지정할 멤버 ID
	 * @param authUser 인증된 사용자 정보
	 * @author alpomjeong
	 */

	@PostMapping("/{targetMemberId}/transfer-owner")
	public ResponseEntity<BaseResponse<Void>> transferOwner(
		@PathVariable Long clubId,
		@PathVariable Long targetMemberId,
		@Auth AuthUser authUser
	) {
		clubMemberService.changeOwnership(clubId, authUser, targetMemberId);
		return ResponseEntity.ok(BaseResponse.success(ResponseCode.OK));
	}
}
