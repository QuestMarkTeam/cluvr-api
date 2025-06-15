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

	/**
	 * InfoProblemFormResponseDto의 모든 필드를 초기화하는 생성자입니다.
	 *
	 * @param problemFormId 문제 폼의 고유 식별자
	 * @param problemTemplate 문제 템플릿 내용
	 * @param submissionInstructions 제출 지침
	 * @param gradingCriteria 채점 기준
	 * @param isActive 문제 폼의 활성화 여부
	 */
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

	/**
	 * 주어진 ProblemForm 엔티티로부터 InfoProblemFormResponseDto 인스턴스를 생성합니다.
	 *
	 * @param problemForm 변환할 ProblemForm 엔티티
	 * @return ProblemForm의 정보를 담은 InfoProblemFormResponseDto 객체
	 */
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
