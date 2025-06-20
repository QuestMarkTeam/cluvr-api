package com.example.cluvrapi.domain.applicationForm.dto.response;

import lombok.Getter;

@Getter
public class CreateProblemFormResponseDto {
	private Long problemFormId;

	public CreateProblemFormResponseDto(Long applicationFormId) {
		this.problemFormId = applicationFormId;
	}

	public static CreateProblemFormResponseDto from(Long applicationFormId) {
		return new CreateProblemFormResponseDto(applicationFormId);
	}
}
