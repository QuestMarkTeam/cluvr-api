package com.example.cluvrapi.domain.applicationForm.controller;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

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
		return ResponseEntity.ok(BaseResponse.success(problemFormResponseDto, ResponseCode.CREATED));
	}

	@GetMapping("/{problemFormId}")
	public ResponseEntity<BaseResponse<InfoProblemFormResponseDto>> findProblemFormById(
		@PathVariable Long clubId,
		@PathVariable Long problemFormId
	) {
		InfoProblemFormResponseDto problemFormResponseDto = problemFormService.findProblemFormById(clubId,
			problemFormId);
		return ResponseEntity.ok(BaseResponse.success(problemFormResponseDto, ResponseCode.OK));
	}

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

	@GetMapping("/active")
	public ResponseEntity<BaseResponse<InfoProblemFormResponseDto>> findActiveProblemFormByClubId(
		@PathVariable Long clubId
	) {
		InfoProblemFormResponseDto problemFormResponseDto = problemFormService.findActiveProblemFormByClubId(
			clubId);
		return ResponseEntity.ok(BaseResponse.success(problemFormResponseDto, ResponseCode.OK));
	}

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

	@DeleteMapping("/{problemFormId}")
	public ResponseEntity<BaseResponse<Void>> deleteProblemForm(
		@Auth AuthUser authUser,
		@PathVariable Long clubId,
		@PathVariable Long problemFormId
	) {
		problemFormService.deleteProblem(authUser.id(), clubId, problemFormId);
		return ResponseEntity.ok(BaseResponse.success(ResponseCode.NO_CONTENT));
	}

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
