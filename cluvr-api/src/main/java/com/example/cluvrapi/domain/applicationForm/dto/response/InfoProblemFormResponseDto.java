package com.example.cluvrapi.domain.applicationForm.dto.response;

import lombok.Getter;

import com.querydsl.core.annotations.QueryProjection;

@Getter
public class InfoProblemFormResponseDto {

	private String problemTemplate;

	private String submissionInstructions;

	private String gradingCriteria;

	@QueryProjection
	public InfoProblemFormResponseDto(String problemTemplate, String submissionInstructions,
		String gradingCriteria) {
		this.problemTemplate = problemTemplate;
		this.submissionInstructions = submissionInstructions;
		this.gradingCriteria = gradingCriteria;
	}
}
