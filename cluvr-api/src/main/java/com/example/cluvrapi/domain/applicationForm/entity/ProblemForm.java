package com.example.cluvrapi.domain.applicationForm.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.SQLDelete;

import com.example.cluvrapi.domain.club.entity.Club;
import com.example.cluvrapi.domain.common.entity.BaseTimeEntity;

@Entity
@Getter
@Table(name = "problem_forms")
@SQLDelete(sql = "UPDATE problem_forms SET is_deleted = true WHERE id = ?")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProblemForm extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * 문제 양식
	 */
	@Column(nullable = false, columnDefinition = "TEXT")
	private String problemTemplate;

	/**
	 * 문제 제출 시 유의사항 및 안내사항
	 */
	@Column(columnDefinition = "TEXT")
	private String submissionInstructions;

	/**
	 * 채점 기준 설명
	 */
	@Column(length = 100)
	private String gradingCriteria;

	/**
	 * 클럽장이 해당 양식을 활성화 상태를 관리할 수 있다.
	 */
	@Column(nullable = false)
	private Boolean isActive = false;

	/**
	 * 문제 양식은 여러개 등록 가능하다.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "club_id", nullable = false)
	private Club club;

	/**
	 * SoftDeleted
	 */
	@Column(nullable = false)
	private Boolean isDeleted = false;

	/**
	 * 생성자 메서드
	 */
	public ProblemForm(String problemTemplate, String submissionInstructions, String gradingCriteria, Boolean isActive,
		Club club) {
		this.problemTemplate = problemTemplate;
		this.submissionInstructions = submissionInstructions;
		this.gradingCriteria = gradingCriteria;
		this.isActive = Boolean.TRUE.equals(isActive);
		this.club = club;
	}

	/**
	 * 문제 양식 수정
	 */
	public void updateProblemForm(String problemTemplate) {
		this.problemTemplate = problemTemplate;
	}

	/**
	 * 유의사항 및 안내문 수정
	 */
	public void updateSubmissionInstructions(String submissionInstructions) {
		this.submissionInstructions = submissionInstructions;
	}

	/**
	 * 채점 방식 수정
	 */
	public void updateGradingCriteria(String gradingCriteria) {
		this.gradingCriteria = gradingCriteria;
	}

	/**
	 * 문제 활성화
	 */
	public void activate() {
		isActive = true;
	}

	/**
	 * 문제 비활성화
	 */
	public void deactivate() {
		isActive = false;
	}
}
