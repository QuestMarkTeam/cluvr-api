package com.example.cluvrapi.domain.join.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.example.cluvrapi.domain.join.enums.FormFieldType;

/**
 * 실제 입력한 필드 값 하나하나 저장
 */

@Entity
@Getter
@Table(name = "join_request_answers")
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
	@ManyToOne
	private JoinRequest joinRequest;

	/**
	 * Form 의 id 값
	 */
	private Long formFieldId;

	/**
	 * Form 의 Target Type
	 */
	private FormFieldType fieldType;

	/**
	 * Answer
	 */
	private String answer;

	public JoinRequestAnswer(JoinRequest joinRequest, Long formFieldId, FormFieldType fieldType, String answer) {
		this.joinRequest = joinRequest;
		this.formFieldId = formFieldId;
		this.fieldType = fieldType;
		this.answer = answer;
	}
}
