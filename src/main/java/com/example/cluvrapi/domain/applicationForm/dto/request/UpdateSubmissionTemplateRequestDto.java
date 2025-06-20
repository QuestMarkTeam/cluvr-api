package com.example.cluvrapi.domain.applicationForm.dto.request;

import jakarta.validation.constraints.NotBlank;

import lombok.Getter;

@Getter
public class UpdateSubmissionTemplateRequestDto {
	@NotBlank(message = "가입 양식을 작성해주세요.")
	private String submissionTemplate;
}
