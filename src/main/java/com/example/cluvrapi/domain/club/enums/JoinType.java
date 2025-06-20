package com.example.cluvrapi.domain.club.enums;

/**
 * 클럽 가입 방식을 나타내는 열거형
 *
 * @author sinyoung0403
 */
public enum JoinType {
	/**
	 * 문제 양식 제출을 통한 가입
	 */
	PROBLEM_FORM,
	/**
	 * 가입 신청서 제출을 통한 가입
	 */
	SUBMISSION_FORM,
	/**
	 * 가입 신청과 동시에 바로 가입 처리
	 */
	DIRECT_JOIN,
	/**
	 * 단순 가입 신청
	 */
	SIMPLE_REQUEST,
	/**
	 * 초대코드를 통한 가입
	 */
	INVITE_CODE
}
