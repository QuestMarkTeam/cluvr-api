package com.example.cluvrapi.domain.applicationForm.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.example.cluvrapi.domain.club.entity.Club;
import com.example.cluvrapi.domain.common.entity.BaseTimeEntity;

@Entity
@Getter
@Table(name = "submission_forms")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SubmissionForm extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * 제출 양식
	 */
	@Column(columnDefinition = "TEXT")
	private String submissionTemplate;

	/**
	 * 클럽
	 */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "club_id", nullable = false)
	private Club club;

	/**
	 * 생성자 메서드
	 */
	public SubmissionForm(String submissionTemplate, Club club) {
		this.submissionTemplate = submissionTemplate;
		this.club = club;
	}

	/****
	 * 제출 양식 템플릿을 새로운 값으로 변경합니다.
	 *
	 * @param submissionTemplate 새로 설정할 제출 양식 템플릿 문자열
	 */
	public void updateSubmissionTemplate(String submissionTemplate) {
		this.submissionTemplate = submissionTemplate;
	}

	/****
	 * 이 제출 양식 엔티티와 연관된 클럽 엔티티를 설정하거나 변경합니다.
	 *
	 * @param club 연관시킬 클럽 엔티티
	 */
	public void setClub(Club club) {
		this.club = club;
	}
}
