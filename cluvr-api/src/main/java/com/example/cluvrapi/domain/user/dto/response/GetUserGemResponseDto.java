package com.example.cluvrapi.domain.user.dto.response;

import lombok.Getter;

@Getter
public class GetUserPointResponseDto {
	private final Integer gem;

	public GetUserPointResponseDto(Integer gem) {
		this.gem = gem;
	}
}
