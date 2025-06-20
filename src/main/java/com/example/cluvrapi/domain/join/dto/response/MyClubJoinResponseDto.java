package com.example.cluvrapi.domain.join.dto.response;

import lombok.Getter;

import com.example.cluvrapi.domain.club.enums.JoinType;
import com.example.cluvrapi.domain.join.enums.JoinStatus;
import com.querydsl.core.annotations.QueryProjection;

/**
 * 클럽 운영진이 조회할 Join Request 정보
 */

@Getter
public class MyClubJoinResponseDto {
	private Long joinRequestId;
	private Long userId;
	private JoinStatus joinStatus;
	private JoinType joinType;

	@QueryProjection
	public MyClubJoinResponseDto(Long joinRequestId, Long userId, JoinStatus joinStatus, JoinType joinType) {
		this.joinRequestId = joinRequestId;
		this.userId = userId;
		this.joinStatus = joinStatus;
		this.joinType = joinType;
	}
}
