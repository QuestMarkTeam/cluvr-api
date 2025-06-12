package com.example.cluvrapi.domain.user.dto.response;

import lombok.Getter;

@Getter
public class GetUserGemResponseDto {
	private final Integer gem;

	public GetUserGemResponseDto(Integer gem) {
		this.gem = gem;
	}
}
