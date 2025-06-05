package com.example.cluvrapi.domain.applicationForm.dto.request;

import jakarta.validation.constraints.NotBlank;

import lombok.Getter;

@Getter
public class UpdateProblemFormRequestDto {
	@NotBlank
	private String problemTemplate;
	@NotBlank
	private String submissionInstructions;
	@NotBlank
	private String gradingCriteria;
}
