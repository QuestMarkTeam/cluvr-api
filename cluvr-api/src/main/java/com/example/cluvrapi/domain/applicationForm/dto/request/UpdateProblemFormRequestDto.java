package com.example.cluvrapi.domain.applicationForm.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Getter;

@Getter
public class UpdateProblemFormRequestDto {
	@NotBlank(message = "문제 양식을 작성해주세요.")
	private String problemTemplate;
	@NotBlank(message = "문제 설명을 작성해주세요.")
	private String submissionInstructions;
	@Size(min = 2, max = 100, message = "체점 방식은 2자 이상 100자 이하로 작성해주세요.")
	private String gradingCriteria;
}
