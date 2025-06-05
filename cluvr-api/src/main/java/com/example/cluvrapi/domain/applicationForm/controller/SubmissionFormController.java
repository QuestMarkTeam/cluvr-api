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
import org.springframework.web.bind.annotation.RestController;

import com.example.cluvrapi.domain.applicationForm.dto.request.CreateSubmissionFormRequestDto;
import com.example.cluvrapi.domain.applicationForm.dto.request.UpdateSubmissionTemplateRequestDto;
import com.example.cluvrapi.domain.applicationForm.dto.response.CreateSubmissionFormResponseDto;
import com.example.cluvrapi.domain.applicationForm.dto.response.InfoSubmissionFormResponseDto;
import com.example.cluvrapi.domain.applicationForm.service.SubmissionFormService;
import com.example.cluvrapi.domain.common.dto.PageResponseDto;
import com.example.cluvrapi.global.response.BaseResponse;
import com.example.cluvrapi.global.response.ResponseCode;

@RestController
@RequiredArgsConstructor
@RequestMapping("/clubs/{clubId}/submission-forms")
public class SubmissionFormController {

	private final SubmissionFormService submissionFormService;

	@PostMapping
	public ResponseEntity<BaseResponse<CreateSubmissionFormResponseDto>> createSubmissionForm(
		@PathVariable Long clubId,
		@Valid @RequestBody CreateSubmissionFormRequestDto submissionFormRequestDto
	) {
		CreateSubmissionFormResponseDto submissionFormResponseDto = submissionFormService.createSubmissionForm(clubId,
			submissionFormRequestDto);
		return ResponseEntity.ok(BaseResponse.success(submissionFormResponseDto, ResponseCode.CREATED));
	}

	@GetMapping("/{submissionFormId}")
	public ResponseEntity<BaseResponse<InfoSubmissionFormResponseDto>> findSubmissionFormById(
		@PathVariable Long clubId,
		@PathVariable Long submissionFormId
	) {
		InfoSubmissionFormResponseDto submissionFormResponseDto = submissionFormService.findSubmissionFormById(clubId,
			submissionFormId);
		return ResponseEntity.ok(BaseResponse.success(submissionFormResponseDto, ResponseCode.OK));
	}

	@GetMapping
	public ResponseEntity<BaseResponse<PageResponseDto<InfoSubmissionFormResponseDto>>> findAllSubmissionForm(
		@PathVariable Long clubId,
		@PageableDefault(size = 5, sort = "createdAt") Pageable pageable
	) {
		PageResponseDto<InfoSubmissionFormResponseDto> pageSubmissionFormResponseDto = submissionFormService.findAllSubmissionForm(
			clubId,
			pageable);
		return ResponseEntity.ok(BaseResponse.success(pageSubmissionFormResponseDto, ResponseCode.OK));
	}

	@PatchMapping("/{submissionFormId}")
	public ResponseEntity<BaseResponse<Void>> updateSubmissionForm(
		@PathVariable Long clubId,
		@PathVariable Long submissionFormId,
		@Valid @RequestBody UpdateSubmissionTemplateRequestDto submissionTemplateRequestDto
	) {
		submissionFormService.updateSubmissionTemplate(clubId, submissionFormId, submissionTemplateRequestDto);
		return ResponseEntity.ok(BaseResponse.success(ResponseCode.NO_CONTENT));
	}

	@DeleteMapping("/{submissionFormId}")
	public ResponseEntity<BaseResponse<Void>> deleteSubmissionForm(
		@PathVariable Long clubId,
		@PathVariable Long submissionFormId
	) {
		submissionFormService.deleteSubmissionForm(clubId, submissionFormId);
		return ResponseEntity.ok(BaseResponse.success(ResponseCode.NO_CONTENT));
	}
}
