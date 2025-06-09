package com.example.cluvrapi.domain.analytics.dto;

import lombok.Getter;

@Getter
public class PointStatRedisDto {

	private Integer point;

	private Long userId;

	public PointStatRedisDto(Integer point, Long userId) {
		this.point = point;
		this.userId = userId;
	}
}
