package com.example.cluvrapi.domain.rank.dto.request;

import lombok.Getter;

import com.example.cluvrapi.domain.rank.eunms.Tier;

@Getter
public class CreateRankRequestDto {
	private Integer score;
	private Tier tier;
	private Long userId;
}
