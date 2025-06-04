package com.example.cluvrapi.domain.point.dto.response;

import lombok.Getter;

@Getter
public class UpdatePointResponseDto {
	private final Integer point;

	public UpdatePointResponseDto(Integer point) {
		this.point = point;
	}

	public static UpdatePointResponseDto from(Integer point) {
		return new UpdatePointResponseDto(point);
	}
}
