package com.example.cluvrapi.domain.join.dto.response;

import lombok.Getter;

@Getter
public class CreateJoinRequestByCodeResponseDto {
	private Long joinRequestId;

	public CreateJoinRequestByCodeResponseDto(Long joinRequestId) {
		this.joinRequestId = joinRequestId;
	}

	public static CreateJoinRequestByCodeResponseDto from(Long joinRequestId) {
		return new CreateJoinRequestByCodeResponseDto(joinRequestId);
	}
}
