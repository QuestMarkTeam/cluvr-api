package com.example.cluvrapi.domain.clover.dto.response;

import lombok.Getter;

import com.example.cluvrapi.domain.clover.enums.Tier;
import com.querydsl.core.annotations.QueryProjection;

@Getter
public class FindCloverResponseDto {

	private final Integer score;

	private final Tier tier;

	private final Long userId;

	@QueryProjection
	public FindCloverResponseDto(Integer score, Tier tier, Long userId) {
		this.score = score;
		this.tier = tier;
		this.userId = userId;
	}
}
