package com.example.cluvrapi.domain.user.dto.response;

import lombok.Getter;

@Getter
public class GetUserPointResponseDto {
	private final Long point;

	public GetUserPointResponseDto(Long point) {
		this.point = point;
	}
}
