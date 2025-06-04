package com.example.cluvrapi.domain.rank.dto;

import lombok.Getter;

import com.example.cluvrapi.domain.rank.eunms.Tier;
import com.querydsl.core.annotations.QueryProjection;

@Getter
public class FindRankResponseDto {

	private final Integer score;

	private final Tier tier;

	private final Long userId;

	@QueryProjection
	public FindRankResponseDto(Integer score, Tier tier, Long userId) {
		this.score = score;
		this.tier = tier;
		this.userId = userId;
	}
}
