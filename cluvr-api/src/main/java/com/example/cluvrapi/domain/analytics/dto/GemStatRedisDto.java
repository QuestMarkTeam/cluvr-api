package com.example.cluvrapi.domain.analytics.dto;

import lombok.Getter;

@Getter
public class GemStatRedisDto {

	private Integer gem;

	private Long userId;

	public GemStatRedisDto(Integer gem, Long userId) {
		this.gem = gem;
		this.userId = userId;
	}
}
