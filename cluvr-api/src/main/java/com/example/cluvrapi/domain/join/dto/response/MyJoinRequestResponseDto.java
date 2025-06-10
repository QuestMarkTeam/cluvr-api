package com.example.cluvrapi.domain.join.dto.response;

import lombok.Getter;

import com.example.cluvrapi.domain.club.enums.JoinType;
import com.example.cluvrapi.domain.join.enums.JoinStatus;
import com.querydsl.core.annotations.QueryProjection;

/**
 * 로그인한 유저가 조회할 Join Request 정보
 */

@Getter
public class MyJoinRequestResponseDto {
	private Long joinRequestId;
	private Long clubId;
	private JoinStatus joinStatus;
	private JoinType joinType;

	@QueryProjection
	public MyJoinRequestResponseDto(Long joinRequestId, Long clubId, JoinStatus joinStatus, JoinType joinType) {
		this.joinRequestId = joinRequestId;
		this.clubId = clubId;
		this.joinStatus = joinStatus;
		this.joinType = joinType;
	}
}
