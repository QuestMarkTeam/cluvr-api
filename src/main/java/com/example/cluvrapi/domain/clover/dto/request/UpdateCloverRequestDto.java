package com.example.cluvrapi.domain.clover.dto.request;

import lombok.Getter;

@Getter
public class UpdateCloverRequestDto {
	private final Integer score;
	private final Long userId;

	public UpdateCloverRequestDto(Integer score, Long userId) {
		this.score = score;
		this.userId = userId;
	}
}
