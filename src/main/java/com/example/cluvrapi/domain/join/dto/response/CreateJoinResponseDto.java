package com.example.cluvrapi.domain.join.dto.response;

import lombok.Getter;

/**
 * 생성할 Join Request 정보
 */

@Getter
public class CreateJoinResponseDto {
	private Long joinRequestId;

	public CreateJoinResponseDto(Long joinRequestId) {
		this.joinRequestId = joinRequestId;
	}

	public static CreateJoinResponseDto from(Long joinRequestId) {
		return new CreateJoinResponseDto(joinRequestId);
	}
}
