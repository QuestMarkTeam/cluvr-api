package com.example.cluvrapi.domain.applicationForm.dto.response;

import lombok.Getter;

@Getter
public class CreateSubmissionFormResponseDto {
	private Long submissionFormId;

	public CreateSubmissionFormResponseDto(Long applicationFormId) {
		this.submissionFormId = applicationFormId;
	}

	public static CreateSubmissionFormResponseDto from(Long applicationFormId) {
		return new CreateSubmissionFormResponseDto(applicationFormId);
	}
}
