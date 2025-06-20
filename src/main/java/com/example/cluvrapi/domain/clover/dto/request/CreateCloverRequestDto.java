package com.example.cluvrapi.domain.clover.dto.request;

import lombok.Getter;

import com.example.cluvrapi.domain.clover.enums.Tier;

@Getter
public class CreateCloverRequestDto {
	private Integer clover;
	private Tier tier;
	private Long userId;

	public CreateCloverRequestDto(Integer clover, Tier tier, Long userId) {
		this.clover = clover;
		this.tier = tier;
		this.userId = userId;
	}

	public static CreateCloverRequestDto from(Integer clover, Tier tier, Long userId) {
		return new CreateCloverRequestDto(clover, tier, userId);
	}

}
