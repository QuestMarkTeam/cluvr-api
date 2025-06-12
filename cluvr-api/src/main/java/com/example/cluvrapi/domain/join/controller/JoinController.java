package com.example.cluvrapi.domain.join.controller;

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
import org.springframework.web.bind.annotation.RestController;

import com.example.cluvrapi.domain.common.annotation.Auth;
import com.example.cluvrapi.domain.common.dto.AuthUser;
import com.example.cluvrapi.domain.common.dto.PageResponseDto;
import com.example.cluvrapi.domain.join.dto.request.CreateJoinRequestByCodeRequestDto;
import com.example.cluvrapi.domain.join.dto.request.CreateJoinRequestDto;
import com.example.cluvrapi.domain.join.dto.request.UpdateJoinRequestDto;
import com.example.cluvrapi.domain.join.dto.response.CreateJoinRequestByCodeResponseDto;
import com.example.cluvrapi.domain.join.dto.response.CreateJoinResponseDto;
import com.example.cluvrapi.domain.join.dto.response.InfoJoinRequestResponseDto;
import com.example.cluvrapi.domain.join.dto.response.MyClubJoinResponseDto;
import com.example.cluvrapi.domain.join.dto.response.MyJoinRequestResponseDto;
import com.example.cluvrapi.domain.join.service.JoinService;
import com.example.cluvrapi.global.response.BaseResponse;
import com.example.cluvrapi.global.response.ResponseCode;

/**
 * 가입 요청 관련 API 를 처리하는 컨트롤러입니다.
 */

@RestController
@RequiredArgsConstructor
public class JoinController {

	private final JoinService joinService;

	/**
	 * 설명: 특정 클럽에 가입 요청을 생성합니다.
	 *
	 * @param authUser       인증된 사용자 정보
	 * @param clubId         가입 요청을 보낼 클럽의 ID
	 * @param joinRequestDto 가입 요청에 필요한 정보
	 * @return 생성된 가입 요청 정보
	 * @author sinyoung0403
	 */

	@PostMapping("/clubs/{clubId}/join")
	public ResponseEntity<BaseResponse<CreateJoinResponseDto>> createJoin(
		@Auth AuthUser authUser,
		@PathVariable Long clubId,
		@Valid @RequestBody CreateJoinRequestDto joinRequestDto
	) {
		CreateJoinResponseDto joinResponseDto = joinService.createJoin(1L, clubId, joinRequestDto);
		return ResponseEntity.ok(BaseResponse.success(joinResponseDto, ResponseCode.CREATED));
	}

	/**
	 * 설명: 특정 클럽에 도착한 모든 가입 요청을 페이징하여 조회합니다.
	 *
	 * @param clubId   클럽 ID
	 * @param pageable 페이징 정보
	 * @return 클럽에 대한 가입 요청 목록
	 * @author sinyoung0403
	 */

	@GetMapping("/clubs/{clubId}/join")
	public ResponseEntity<BaseResponse<PageResponseDto<MyClubJoinResponseDto>>> findAllJoinRequestByClubId(
		@PathVariable Long clubId,
		@PageableDefault Pageable pageable
	) {
		PageResponseDto<MyClubJoinResponseDto> joinResponseDto = joinService.findJoinRequestByClubId(clubId, pageable);
		return ResponseEntity.ok(BaseResponse.success(joinResponseDto, ResponseCode.OK));
	}

	/**
	 * 설명: 현재 로그인한 사용자가 보낸 모든 가입 요청을 페이징하여 조회합니다.
	 *
	 * @param authUser 인증된 사용자 정보
	 * @param pageable 페이징 정보
	 * @return 본인이 보낸 가입 요청 목록
	 * @author sinyoung0403
	 */

	@GetMapping("/clubs/join/my")
	public ResponseEntity<BaseResponse<PageResponseDto<MyJoinRequestResponseDto>>> findAllMyJoinRequests(
		@Auth AuthUser authUser,
		@PageableDefault Pageable pageable
	) {
		PageResponseDto<MyJoinRequestResponseDto> joinResponseDto = joinService.findMyJoinRequests(1L,
			pageable);
		return ResponseEntity.ok(BaseResponse.success(joinResponseDto, ResponseCode.OK));
	}

	/**
	 * 설명: 클럽과 가입 요청 ID에 해당하는 가입 요청 상세 정보를 조회합니다.
	 *
	 * <p> 가입 양식(Answer)의 경우 응답이 없을 경우 해당 컬럼이 "null" 로 조회됩니다.
	 *
	 * @param clubId        클럽 ID
	 * @param joinRequestId 가입 요청 ID
	 * @return 가입 요청 상세 정보
	 * @author sinyoung0403
	 */

	@GetMapping("/clubs/{clubId}/join/{joinRequestId}")
	public ResponseEntity<BaseResponse<InfoJoinRequestResponseDto>> findJoinRequestById(
		@PathVariable Long clubId,
		@PathVariable Long joinRequestId
	) {
		InfoJoinRequestResponseDto joinResponseDto = joinService.findJoinRequestById(clubId,
			joinRequestId);
		return ResponseEntity.ok(BaseResponse.success(joinResponseDto, ResponseCode.OK));
	}

	/**
	 * 설명: 클럽과 가입 요청 ID에 해당하는 가입 요청의 답변을 수정합니다.
	 *
	 * @param clubId               클럽 ID
	 * @param joinRequestId        가입 요청 ID
	 * @param updateJoinRequestDto 수정할 답변 정보
	 * @return 처리 결과
	 * @author sinyoung0403
	 */

	@PatchMapping("/clubs/{clubId}/join/{joinRequestId}")
	public ResponseEntity<BaseResponse<Void>> updateJoinRequestAnswer(
		@PathVariable Long clubId,
		@PathVariable Long joinRequestId,
		@RequestBody UpdateJoinRequestDto updateJoinRequestDto
	) {
		joinService.updateJoinRequestAnswer(clubId, joinRequestId, updateJoinRequestDto);
		return ResponseEntity.ok(BaseResponse.success(ResponseCode.NO_CONTENT));
	}

	/**
	 * 특정 클럽에 보낸 가입 요청을 취소합니다.
	 *
	 * @param clubId        클럽 ID
	 * @param joinRequestId 가입 요청 ID
	 * @return No content
	 * @author sinyoung0403
	 */

	@DeleteMapping("/clubs/{clubId}/join/{joinRequestId}")
	public ResponseEntity<BaseResponse<Void>> cancelJoinRequest(
		@PathVariable Long clubId,
		@PathVariable Long joinRequestId
	) {
		joinService.cancelJoinRequest(clubId, joinRequestId);
		return ResponseEntity.ok(BaseResponse.success(ResponseCode.NO_CONTENT));
	}

	/**
	 * 설명: 초대코드를 이용한 클럽 가입 요청합니다.
	 *
	 * <p> 사용자가 초대코드를 입력하여 특정 클럽에 가입 요청을 보냅니다.
	 * 유효한 초대코드가 존재해야 요청이 처리됩니다.
	 *
	 * @param authUser                          인증된 사용자 정보 (토큰 기반)
	 * @param createJoinRequestByCodeRequestDto 초대코드를 포함한 클럽 가입 요청 데이터
	 * @return 클럽 가입 요청 결과 정보 (성공 시 JoinRequest ID 반환)
	 * @author sinyoung0403
	 */
	@PostMapping("/clubs/join-with-codes")
	public ResponseEntity<BaseResponse<CreateJoinRequestByCodeResponseDto>> createJoinRequestByInviteCode(
		@Auth AuthUser authUser,
		@Valid @RequestBody CreateJoinRequestByCodeRequestDto createJoinRequestByCodeRequestDto
	) {
		CreateJoinRequestByCodeResponseDto joinRequestByInviteCodeDto = joinService.createJoinRequestByInviteCode(
			authUser.id(), createJoinRequestByCodeRequestDto);
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(BaseResponse.success(joinRequestByInviteCodeDto, ResponseCode.CREATED));
	}
}
