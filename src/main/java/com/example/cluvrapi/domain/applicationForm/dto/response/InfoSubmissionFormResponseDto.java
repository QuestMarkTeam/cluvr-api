package com.example.cluvrapi.domain.applicationForm.dto.response;

import lombok.Getter;

import com.querydsl.core.annotations.QueryProjection;

@Getter
public class InfoSubmissionFormResponseDto {
	private String submissionTemplate;

	@QueryProjection
	public InfoSubmissionFormResponseDto(String submissionTemplate) {
		this.submissionTemplate = submissionTemplate;
	}
}
