package com.example.cluvrapi.domain.clover.dto.request;

import lombok.Getter;

import com.example.cluvrapi.domain.clover.enums.Tier;

@Getter
public class CreateCloverRequestDto {
	private Integer score;
	private Tier tier;
	private Long userId;
}
