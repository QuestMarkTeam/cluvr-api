package com.example.cluvrapi.domain.point.dto.response;

import lombok.Getter;

@Getter
public class CreatePointResponseDto {
	private final Integer amount;

	public CreatePointResponseDto(Integer amount) {
		this.amount = amount;
	}

	public static CreatePointResponseDto from(Integer amount) {
		return new CreatePointResponseDto(amount);
	}
}
