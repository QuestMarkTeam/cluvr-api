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
import com.example.cluvrapi.domain.common.annotation.Auth;
import com.example.cluvrapi.domain.common.dto.AuthUser;
import com.example.cluvrapi.domain.common.dto.PageResponseDto;
import com.example.cluvrapi.global.response.BaseResponse;
import com.example.cluvrapi.global.response.ResponseCode;

@RestController
@RequiredArgsConstructor
@RequestMapping("/clubs/{clubId}/submission-forms")
public class SubmissionFormController {

	private final SubmissionFormService submissionFormService;

	/**
	 * 새로운 동아리 지원서 양식을 생성합니다.
	 *
	 * 인증된 사용자의 정보와 동아리 ID, 지원서 양식 생성 요청 정보를 받아 해당 동아리에 지원서 양식을 생성합니다.
	 *
	 * @param clubId 지원서 양식을 생성할 동아리의 ID
	 * @param submissionFormRequestDto 생성할 지원서 양식의 정보가 담긴 요청 객체
	 * @return 생성된 지원서 양식 정보와 함께 성공 응답을 반환합니다.
	 */
	@PostMapping
	public ResponseEntity<BaseResponse<CreateSubmissionFormResponseDto>> createSubmissionForm(
		@Auth AuthUser authUser,
		@PathVariable Long clubId,
		@Valid @RequestBody CreateSubmissionFormRequestDto submissionFormRequestDto
	) {
		CreateSubmissionFormResponseDto submissionFormResponseDto = submissionFormService.createSubmissionForm(
			authUser.id(),
			clubId,
			submissionFormRequestDto
		);
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

	/**
	 * 클럽 내 특정 제출 양식 템플릿을 수정합니다.
	 *
	 * @param clubId 클럽의 고유 식별자
	 * @param submissionFormId 수정할 제출 양식의 고유 식별자
	 * @param submissionTemplateRequestDto 제출 양식 수정 요청 데이터
	 * @return 성공 시 내용 없는 응답을 반환합니다.
	 */
	@PatchMapping("/{submissionFormId}")
	public ResponseEntity<BaseResponse<Void>> updateSubmissionForm(
		@Auth AuthUser authUser,
		@PathVariable Long clubId,
		@PathVariable Long submissionFormId,
		@Valid @RequestBody UpdateSubmissionTemplateRequestDto submissionTemplateRequestDto
	) {
		submissionFormService.updateSubmissionTemplate(authUser.id(), clubId, submissionFormId,
			submissionTemplateRequestDto);
		return ResponseEntity.ok(BaseResponse.success(ResponseCode.NO_CONTENT));
	}

	/**
	 * 클럽 내 특정 지원서 양식을 삭제합니다.
	 *
	 * @param clubId 클럽의 고유 식별자
	 * @param submissionFormId 삭제할 지원서 양식의 고유 식별자
	 * @return 삭제 성공 시 내용 없는 성공 응답을 반환합니다.
	 */
	@DeleteMapping("/{submissionFormId}")
	public ResponseEntity<BaseResponse<Void>> deleteSubmissionForm(
		@Auth AuthUser authUser,
		@PathVariable Long clubId,
		@PathVariable Long submissionFormId
	) {
		submissionFormService.deleteSubmissionForm(authUser.id(), clubId, submissionFormId);
		return ResponseEntity.ok(BaseResponse.success(ResponseCode.NO_CONTENT));
	}
}
