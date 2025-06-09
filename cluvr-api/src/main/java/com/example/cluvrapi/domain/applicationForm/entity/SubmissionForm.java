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
@Table(name = "submission_forms")
@SQLDelete(sql = "UPDATE submission_forms SET is_deleted = true WHERE id = ?")
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
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "club_id", nullable = false)
	private Club club;

	/**
	 * Soft Deleted
	 */
	@Column(nullable = false)
	private Boolean isDeleted = false;

	/**
	 * 생성자 메서드
	 */
	public SubmissionForm(String submissionTemplate, Club club) {
		this.submissionTemplate = submissionTemplate;
		this.club = club;
	}

	/**
	 * 가입 양식 수정
	 */
	public void updateSubmissionTemplate(String submissionTemplate) {
		this.submissionTemplate = submissionTemplate;
	}
}
