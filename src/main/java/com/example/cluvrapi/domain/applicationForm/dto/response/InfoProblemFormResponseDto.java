package com.example.cluvrapi.domain.applicationForm.dto.response;

import lombok.Getter;

import com.example.cluvrapi.domain.applicationForm.entity.ProblemForm;
import com.querydsl.core.annotations.QueryProjection;

@Getter
public class InfoProblemFormResponseDto {

	private Long problemFormId;

	private String problemTemplate;

	private String submissionInstructions;

	private String gradingCriteria;

	private Boolean isActive;

	@QueryProjection
	public InfoProblemFormResponseDto(
		Long problemFormId,
		String problemTemplate,
		String submissionInstructions,
		String gradingCriteria,
		Boolean isActive) {
		this.problemFormId = problemFormId;
		this.problemTemplate = problemTemplate;
		this.submissionInstructions = submissionInstructions;
		this.gradingCriteria = gradingCriteria;
		this.isActive = isActive;
	}

	public static InfoProblemFormResponseDto from(ProblemForm problemForm) {
		return new InfoProblemFormResponseDto(
			problemForm.getId(),
			problemForm.getProblemTemplate(),
			problemForm.getSubmissionInstructions(),
			problemForm.getGradingCriteria(),
			problemForm.getIsActive()
		);
	}
}
