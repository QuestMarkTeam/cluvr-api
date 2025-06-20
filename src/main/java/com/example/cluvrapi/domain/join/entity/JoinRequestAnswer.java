package com.example.cluvrapi.domain.join.entity;

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

import com.example.cluvrapi.domain.join.enums.FormFieldType;

/**
 * 사용자가 가입 신청할 때, 작성한 답변을 저장합니다.
 * 가입 양식이 달라도 같은 테이블에서 Data 를 관리합니다.
 */

@Entity
@Getter
@Table(name = "join_request_answers")
@SQLDelete(sql = "UPDATE join_request_answers SET is_deleted = true WHERE id = ?")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JoinRequestAnswer {

	/**
	 * 식별자
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * 신청 데이터
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "join_request_id", nullable = false)
	private JoinRequest joinRequest;

	/**
	 * Form 의 id 값
	 */
	@Column(nullable = false)
	private Long formFieldId;

	/**
	 * Form 의 Target Type :
	 * SUBMISSION, PROBLEM
	 */
	@Column(nullable = false)
	private FormFieldType fieldType;

	/**
	 * Answer
	 */
	@Column(nullable = false, columnDefinition = "TEXT")
	private String answer;

	/**
	 * Soft Deleted
	 */
	@Column(nullable = false)
	private boolean isDeleted = false;

	/**
	 * 설명: 생성자 메서드
	 *
	 * @param joinRequest 가입 신청
	 * @param formFieldId 가입 신청 답변의 식별자
	 * @param fieldType   가입 양식 타입
	 * @param answer      답변
	 * @author sinyoung0403
	 */
	public JoinRequestAnswer(JoinRequest joinRequest, Long formFieldId, FormFieldType fieldType, String answer) {
		this.joinRequest = joinRequest;
		this.formFieldId = formFieldId;
		this.fieldType = fieldType;
		this.answer = answer;
	}

	/**
	 * 설명: 답변을 수정해주는 메서드
	 *
	 * @param answer 수정하고 싶은 답변
	 * @author sinyoung0403
	 */
	public void updateAnswer(String answer) {
		this.answer = answer;
	}
}
