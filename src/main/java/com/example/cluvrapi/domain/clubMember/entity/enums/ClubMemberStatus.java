package com.example.cluvrapi.domain.clubMember.entity.enums;

/**
 * 클럽 멤버의 현재 소속 상태를 나타내는 열거형(enum)입니다.
 */
public enum ClubMemberStatus {

	/**
	 * 클럽에 정상적으로 소속된 상태
	 */
	ACTIVE,

	/**
	 * 운영자에 의해 클럽에서 강제 탈퇴(강퇴)된 상태
	 */
	KICKED,

	/**
	 * 사용자가 자발적으로 클럽을 탈퇴한 상태
	 */
	WITHDRAWN
}
