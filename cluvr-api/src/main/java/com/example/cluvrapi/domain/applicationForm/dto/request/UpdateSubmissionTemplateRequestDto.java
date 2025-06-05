package com.example.cluvrapi.domain.applicationForm.dto.request;

import jakarta.validation.constraints.NotBlank;

import lombok.Getter;

@Getter
public class UpdateSubmissionTemplateRequestDto {
	@NotBlank
	private String submissionTemplate;
}
