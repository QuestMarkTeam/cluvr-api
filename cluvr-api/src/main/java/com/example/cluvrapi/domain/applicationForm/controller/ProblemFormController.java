package com.example.cluvrapi.domain.applicationForm.controller;

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

import com.example.cluvrapi.domain.applicationForm.dto.request.CreateProblemFormRequestDto;
import com.example.cluvrapi.domain.applicationForm.dto.request.UpdateProblemFormRequestDto;
import com.example.cluvrapi.domain.applicationForm.dto.response.CreateProblemFormResponseDto;
import com.example.cluvrapi.domain.applicationForm.dto.response.InfoProblemFormResponseDto;
import com.example.cluvrapi.domain.applicationForm.service.ProblemFormService;
import com.example.cluvrapi.domain.common.annotation.Auth;
import com.example.cluvrapi.domain.common.dto.AuthUser;
import com.example.cluvrapi.domain.common.dto.PageResponseDto;
import com.example.cluvrapi.global.response.BaseResponse;
import com.example.cluvrapi.global.response.ResponseCode;

@RestController
@RequiredArgsConstructor
@RequestMapping("/clubs/{clubId}/problem-forms")
public class ProblemFormController {
	private final ProblemFormService problemFormService;

	/**
	 * 새로운 문제 폼을 생성하여 반환합니다.
	 *
	 * 인증된 사용자의 정보와 클럽 ID, 문제 폼 생성 요청 데이터를 받아 문제 폼을 생성하고, 생성된 문제 폼 정보를 포함한 응답을 반환합니다.
	 *
	 * @param clubId 문제 폼을 생성할 클럽의 ID
	 * @param problemFormRequestDto 문제 폼 생성 요청 데이터
	 * @return 생성된 문제 폼 정보가 포함된 201 Created 응답
	 */
	@PostMapping
	public ResponseEntity<BaseResponse<CreateProblemFormResponseDto>> createProblemForm(
		@Auth AuthUser authUser,
		@PathVariable Long clubId,
		@Valid @RequestBody CreateProblemFormRequestDto problemFormRequestDto
	) {
		CreateProblemFormResponseDto problemFormResponseDto = problemFormService.createProblemForm(
			authUser.id(),
			clubId,
			problemFormRequestDto
		);
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(BaseResponse.success(problemFormResponseDto, ResponseCode.CREATED));
	}

	/**
	 * 특정 클럽의 문제 폼 상세 정보를 조회합니다.
	 *
	 * @param clubId 클럽의 고유 식별자
	 * @param problemFormId 조회할 문제 폼의 고유 식별자
	 * @return 문제 폼 상세 정보를 담은 응답
	 */
	@GetMapping("/{problemFormId}")
	public ResponseEntity<BaseResponse<InfoProblemFormResponseDto>> findProblemFormById(
		@PathVariable Long clubId,
		@PathVariable Long problemFormId
	) {
		InfoProblemFormResponseDto problemFormResponseDto = problemFormService.findProblemFormById(clubId,
			problemFormId);
		return ResponseEntity.ok(BaseResponse.success(problemFormResponseDto, ResponseCode.OK));
	}

	/**
	 * 클럽 내 모든 문제 폼을 페이지네이션하여 조회합니다.
	 *
	 * @param clubId 조회할 클럽의 ID
	 * @param pageable 페이지네이션 및 정렬 정보 (기본: 5개씩, 생성일 기준 정렬)
	 * @return 문제 폼 목록의 페이지네이션 결과가 포함된 응답
	 */
	@GetMapping
	public ResponseEntity<BaseResponse<PageResponseDto<InfoProblemFormResponseDto>>> findAllProblemForm(
		@PathVariable Long clubId,
		@PageableDefault(size = 5, sort = "createdAt") Pageable pageable
	) {
		PageResponseDto<InfoProblemFormResponseDto> pageProblemFormResponseDto = problemFormService.findAllProblemForm(
			clubId,
			pageable);
		return ResponseEntity.ok(BaseResponse.success(pageProblemFormResponseDto, ResponseCode.OK));
	}

	/**
	 * 주어진 클럽의 활성화된 문제 폼을 조회합니다.
	 *
	 * @param clubId 클럽의 고유 식별자
	 * @return 활성화된 문제 폼 정보가 포함된 응답
	 */
	@GetMapping("/active")
	public ResponseEntity<BaseResponse<InfoProblemFormResponseDto>> findActiveProblemFormByClubId(
		@PathVariable Long clubId
	) {
		InfoProblemFormResponseDto problemFormResponseDto = problemFormService.findActiveProblemFormByClubId(
			clubId);
		return ResponseEntity.ok(BaseResponse.success(problemFormResponseDto, ResponseCode.OK));
	}

	/**
	 * 문제 폼을 수정합니다.
	 *
	 * 클럽 ID와 문제 폼 ID로 특정 문제 폼을 찾아, 인증된 사용자의 요청에 따라 수정합니다.
	 * 성공 시 204 No Content 상태의 응답을 반환합니다.
	 */
	@PatchMapping("/{problemFormId}")
	public ResponseEntity<BaseResponse<Void>> updateProblemForm(
		@Auth AuthUser authUser,
		@PathVariable Long clubId,
		@PathVariable Long problemFormId,
		@Valid @RequestBody UpdateProblemFormRequestDto updateProblemFormRequestDto
	) {
		problemFormService.updateProblemForm(authUser.id(), clubId, problemFormId, updateProblemFormRequestDto);
		return ResponseEntity.ok(BaseResponse.success(ResponseCode.NO_CONTENT));
	}

	/**
	 * 클럽 내 특정 문제 폼을 삭제합니다.
	 *
	 * @param clubId 삭제할 문제 폼이 속한 클럽의 ID
	 * @param problemFormId 삭제할 문제 폼의 ID
	 * @return 삭제 성공 시 204 No Content 상태의 응답
	 */
	@DeleteMapping("/{problemFormId}")
	public ResponseEntity<BaseResponse<Void>> deleteProblemForm(
		@Auth AuthUser authUser,
		@PathVariable Long clubId,
		@PathVariable Long problemFormId
	) {
		problemFormService.deleteProblem(authUser.id(), clubId, problemFormId);
		return ResponseEntity.ok(BaseResponse.success(ResponseCode.NO_CONTENT));
	}

	/**
	 * 문제 폼의 활성화 상태를 변경합니다.
	 *
	 * @param clubId 클럽의 식별자
	 * @param problemFormId 변경할 문제 폼의 식별자
	 * @param active 활성화 여부(true: 활성화, false: 비활성화)
	 * @return 성공 시 204 No Content 상태의 응답을 반환합니다.
	 */
	@PatchMapping("/{problemFormId}/activation")
	public ResponseEntity<BaseResponse<Void>> changeActivationState(
		@Auth AuthUser authUser,
		@PathVariable Long clubId,
		@PathVariable Long problemFormId,
		@RequestParam Boolean active
	) {
		problemFormService.changeActivationState(authUser.id(), clubId, problemFormId, active);
		return ResponseEntity.ok(BaseResponse.success(ResponseCode.NO_CONTENT));
	}
}
