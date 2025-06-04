package com.example.cluvrapi.domain.til.dto.response;

import lombok.Getter;

@Getter
public class CreateTilResponseDto {
	private Long id;

	public CreateTilResponseDto(Long id) {
		this.id = id;
	}

	// 정적 메서드
	public static CreateTilResponseDto from(Long tilId) {
		return new CreateTilResponseDto(tilId);
	}
}
