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
	 * 클럽 가입 요청을 승인하거나 거절합니다.
	 *
	 * 클럽 ID와 가입 요청 ID, 요청 상태 정보를 받아 해당 가입 요청의 상태를 변경합니다.
	 *
	 * @return 처리 결과에 대한 성공 응답
	 */

	@PostMapping("/join-requests/{joinRequestId}/status")
	public ResponseEntity<BaseResponse<Void>> handleJoinStatus(
		@PathVariable Long clubId,
		@PathVariable Long joinRequestId,
		@RequestBody HandleJoinStatusRequestDto dto,
		@Auth AuthUser authUser
	) {
		clubMemberService.handleJoinRequest(clubId, joinRequestId, dto, authUser);
		return ResponseEntity
			.ok(BaseResponse.success(ResponseCode.OK));
	}

	/**
	 * 클럽 내 특정 멤버의 역할을 변경합니다.
	 *
	 * @param clubId 클럽의 고유 식별자입니다.
	 * @param targetMemberId 역할을 변경할 대상 멤버의 고유 식별자입니다.
	 * @param dto 변경할 새로운 역할 정보를 담고 있는 객체입니다.
	 * @param authUser 인증된 사용자 정보입니다.
	 * @return 역할 변경 성공 시 성공 응답을 반환합니다.
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
	 * 현재 로그인한 사용자가 해당 클럽에서 탈퇴하도록 처리합니다.
	 *
	 * 클럽 ID와 인증된 사용자 정보를 받아 사용자의 클럽 탈퇴를 수행합니다.
	 *
	 * @param clubId 클럽의 고유 식별자
	 * @param authUser 인증된 사용자 정보
	 * @return 클럽 탈퇴 완료 메시지와 성공 응답 코드가 포함된 ResponseEntity
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
	 * 클럽에서 지정한 멤버를 강제로 탈퇴시킵니다.
	 *
	 * @param clubId 클럽의 고유 식별자
	 * @param memberId 강퇴할 멤버의 고유 식별자
	 * @param authUser 인증된 사용자 정보
	 * @return 강퇴 완료 메시지와 함께 성공 응답을 반환합니다.
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
	 * 클럽의 멤버 목록을 페이징하여 조회합니다.
	 *
	 * 클럽 ID에 해당하는 클럽의 멤버 정보를 페이지 단위로 반환합니다.
	 *
	 * @param clubId 조회할 클럽의 ID
	 * @param pageable 페이지 및 정렬 정보
	 * @return 페이징된 클럽 멤버 정보 목록이 포함된 응답
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
	 * 특정 유저의 클럽 내 역할 정보를 조회한다.
	 *
	 * @param clubId 클럽의 고유 식별자
	 * @param targetUserId 역할을 조회할 대상 유저의 고유 식별자
	 * @param authUser 인증된 사용자 정보
	 * @return 해당 유저의 클럽 내 역할 정보를 담은 응답
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
	 * 클럽의 소유권을 지정한 멤버에게 이양합니다.
	 *
	 * @param clubId 클럽의 고유 식별자
	 * @param targetMemberId 새 클럽장으로 지정할 멤버의 고유 식별자
	 * @param authUser 인증된 사용자 정보
	 * @return 소유권 이양 성공 시 성공 응답을 반환합니다.
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
