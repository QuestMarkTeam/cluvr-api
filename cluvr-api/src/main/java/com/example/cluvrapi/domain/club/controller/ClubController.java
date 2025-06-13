package com.example.cluvrapi.domain.club.controller;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.cluvrapi.domain.club.dto.request.CreateClubRequestDto;
import com.example.cluvrapi.domain.club.dto.request.UpdateClubRequestDto;
import com.example.cluvrapi.domain.club.dto.request.UpgradeMemberCountRequestDto;
import com.example.cluvrapi.domain.club.dto.response.CreateClubResponseDto;
import com.example.cluvrapi.domain.club.dto.response.CreateInviteCodeResponseDto;
import com.example.cluvrapi.domain.club.dto.response.FindAllClubResponseDto;
import com.example.cluvrapi.domain.club.dto.response.FindClubResponseDto;
import com.example.cluvrapi.domain.club.enums.ClubType;
import com.example.cluvrapi.domain.club.service.ClubService;
import com.example.cluvrapi.domain.common.annotation.Auth;
import com.example.cluvrapi.domain.common.dto.AuthUser;
import com.example.cluvrapi.domain.common.dto.PageResponseDto;
import com.example.cluvrapi.global.response.BaseResponse;
import com.example.cluvrapi.global.response.ResponseCode;

/**
 * 클럽 관련 요청을 처리하는 컨트롤러
 *
 * <p> 클럽 생성, 조회, 수정, 삭제 등의 기능을 제공합니다.
 * 기본 요청 경로는 {@code /clubs}입니다.
 *
 * @author sinyoung0403
 */

@RestController
@RequestMapping("/clubs")
@RequiredArgsConstructor
public class ClubController {

	private final ClubService clubService;

	/**
	 * 설명: 클럽을 생성합니다.
	 *
	 * <p> 요청한 유저를 클럽장으로 하여 새 클럽을 생성합니다.
	 *
	 * @param authUser             인증된 사용자 정보 (토큰 기반)
	 * @param createClubRequestDto 클럽 생성 요청 정보가 담긴 DTO
	 * @return 생성된 클럽의 ID 정보를 담은 응답 DTO
	 * @author sinyoung0403
	 */

	@PostMapping
	public ResponseEntity<BaseResponse<CreateClubResponseDto>> createClub(
		@Auth AuthUser authUser,
		@Valid @RequestBody CreateClubRequestDto createClubRequestDto
	) {
		CreateClubResponseDto dto = clubService.createClub(
			authUser.id(),
			createClubRequestDto
		);
		return ResponseEntity.ok(BaseResponse.success(dto, ResponseCode.CREATED));
	}

	/**
	 * 클럽 단건을 조회합니다.
	 *
	 * <p> 클럽의 고유 ID를 기반으로 해당 클럽 정보를 조회합니다.
	 *
	 * @param clubId 조회할 클럽의 고유 식별자
	 * @return 단건 클럽 정보가 담긴 응답 DTO
	 * @author sinyoung0403
	 */

	@GetMapping("/{clubId}")
	public ResponseEntity<BaseResponse<FindClubResponseDto>> findClubById(
		@PathVariable Long clubId
	) {
		FindClubResponseDto dto = clubService.findClubById(clubId);
		return ResponseEntity.ok(BaseResponse.success(dto, ResponseCode.OK));
	}

	/**
	 * 설명: 클럽 목록을 조회합니다.
	 *
	 * <p> 클럽 타입을 기준으로 페이징 처리된 클럽 목록을 조회합니다.
	 *
	 * @param clubType 클럽 타입(STUDY, PROJECT, COMMUNITY 등)
	 * @param pageable 페이지네이션 정보 (기본: 5개, createdAt 기준 정렬)
	 * @return 페이징된 클럽 목록 응답 DTO
	 * @author sinyoung0403
	 */

	@GetMapping
	public ResponseEntity<BaseResponse<PageResponseDto<FindAllClubResponseDto>>> findAllClub(
		@RequestParam(defaultValue = "STUDY") ClubType clubType,
		@PageableDefault(size = 5, sort = "createdAt") Pageable pageable
	) {
		PageResponseDto<FindAllClubResponseDto> dto = clubService.findAllClub(clubType, pageable);
		return ResponseEntity.ok(BaseResponse.success(dto, ResponseCode.OK));
	}

	/**
	 * 설명: 클럽 정보를 수정합니다.
	 *
	 * <p>클럽의 이름, 소개말, 설명을 수정할 수 있습니다.
	 *
	 * @param clubId            수정할 클럽의 고유 식별자
	 * @param updateClubRequest 수정할 클럽 정보가 담긴 DTO
	 * @return 성공 응답 반환 (본문 없음)
	 * @author sinyoung0403
	 */

	@PatchMapping("/{clubId}")
	public ResponseEntity<BaseResponse<Void>> updateClub(
		@PathVariable Long clubId,
		@Valid @RequestBody UpdateClubRequestDto updateClubRequest
	) {
		clubService.updateClub(clubId, updateClubRequest);
		return ResponseEntity.ok(BaseResponse.success(ResponseCode.OK));
	}

	/**
	 * 설명: 클럽을 삭제합니다.
	 *
	 * <p> 해당 클럽은 Soft Delete 방식으로 삭제 처리됩니다.
	 *
	 * @param clubId 삭제할 클럽의 고유 식별자
	 * @return 성공 응답 반환 (본문 없음)
	 * @author sinyoung0403
	 */

	@DeleteMapping("/{clubId}")
	public ResponseEntity<BaseResponse<Void>> deleteClub(
		@PathVariable Long clubId
	) {
		clubService.deleteClub(clubId);
		return ResponseEntity.ok(BaseResponse.success(ResponseCode.OK));
	}

	/**
	 * 설명: 클럽의 최대 인원수를 증가시킵니다. (Gem 미사용)
	 *
	 * @param authUser 인증된 사용자 정보
	 * @param clubId   최대 인원수를 증가시킬 클럽의 고유 식별자
	 * @return 성공 응답 반환 (본문 없음, 상태 코드 204 NO_CONTENT)
	 * @author sinyoung0403
	 */

	@PatchMapping("/{clubId}/member-count/upgrade")
	public ResponseEntity<BaseResponse<Void>> upgradeMemberCount(
		@Auth AuthUser authUser,
		@PathVariable("clubId") Long clubId,
		@Valid @RequestBody UpgradeMemberCountRequestDto memberCountRequestDto
	) {
		clubService.upgradeMemberCount(authUser.id(), clubId, memberCountRequestDto);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(BaseResponse.success(ResponseCode.NO_CONTENT));
	}

	/**
	 * 설명: 클럽의 최대 인원수를 증가시킵니다. (Gem 사용)
	 *
	 * @param authUser 인증된 사용자 정보
	 * @param clubId   최대 인원수를 증가시킬 클럽의 고유 식별자
	 * @return 성공 응답 반환 (본문 없음, 상태 코드 204 NO_CONTENT)
	 * @author sinyoung0403
	 */

	@PatchMapping("/{clubId}/member-count/upgrade-with-gems")
	public ResponseEntity<BaseResponse<Void>> upgradeMemberCountWithGem(
		@Auth AuthUser authUser,
		@PathVariable("clubId") Long clubId
	) {
		clubService.upgradeMemberCountWithGem(authUser.id(), clubId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(BaseResponse.success(ResponseCode.NO_CONTENT));
	}

	/**
	 * 설명: 클럽 초대코드 생성합니다.
	 *
	 * <p> 요청한 사용자가 특정 클럽에 대해 초대코드를 생성할 수 있습니다.
	 * 생성된 초대코드는 다른 사용자가 클럽에 가입할 때 사용할 수 있습니다.
	 * 해당 클럽의 생성자 또는 관리자만 초대코드를 생성할 수 있습니다.
	 *
	 * @param authUser 인증된 사용자 정보 (토큰 기반)
	 * @param clubId   초대코드를 생성할 대상 클럽의 고유 ID
	 * @return 초대코드 및 유효시간 등의 정보를 포함한 응답
	 * @author sinyoung0403
	 */

	@PostMapping("/{clubId}/invite-code")
	public ResponseEntity<BaseResponse<CreateInviteCodeResponseDto>> createInviteCode(
		@Auth AuthUser authUser,
		@PathVariable("clubId") Long clubId
	) {
		CreateInviteCodeResponseDto createInviteCodeResponseDto = clubService.createInviteCode(authUser.id(), clubId);
		return ResponseEntity.ok(BaseResponse.success(createInviteCodeResponseDto, ResponseCode.OK));
	}
}
