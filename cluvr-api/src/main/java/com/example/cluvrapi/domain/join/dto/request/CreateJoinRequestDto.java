package com.example.cluvrapi.domain.join.dto.request;

import lombok.Getter;

import com.example.cluvrapi.domain.club.enums.JoinType;

@Getter
public class CreateJoinRequestDto {
	/**
	 * 클럽의 Join Type 과 가입 신청 할 때의 Join Type 이 다를 수 있기 때문에 받는 것이다.
	 */
	private JoinType joinType;

	private String answer;
}
