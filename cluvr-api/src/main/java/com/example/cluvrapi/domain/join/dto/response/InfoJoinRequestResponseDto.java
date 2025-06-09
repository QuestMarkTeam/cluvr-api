package com.example.cluvrapi.domain.join.dto.response;

import lombok.Getter;

import com.example.cluvrapi.domain.club.enums.JoinType;
import com.querydsl.core.annotations.QueryProjection;

/**
 * Join Request 상세 정보
 */

@Getter
public class InfoJoinRequestResponseDto {

	private Long joinRequestId;

	private Long clubId;

	private Long userId;

	private JoinType joinType;

	private String answer;

	@QueryProjection
	public InfoJoinRequestResponseDto(Long joinRequestId, Long clubId, Long userId, JoinType joinType, String answer) {
		this.joinRequestId = joinRequestId;
		this.clubId = clubId;
		this.userId = userId;
		this.joinType = joinType;
		this.answer = answer;
	}
}
