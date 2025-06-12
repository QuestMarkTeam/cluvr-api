package com.example.cluvrapi.domain.gem.dto.response;

import lombok.Getter;

@Getter
public class UpdateGemResponseDto {
	private final Integer gem;

	public UpdateGemResponseDto(Integer gem) {
		this.gem = gem;
	}

	public static UpdateGemResponseDto from(Integer gem) {
		return new UpdateGemResponseDto(gem);
	}
}
