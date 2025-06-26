package com.example.cluvrapi.domain.gem.dto.request;


import lombok.Getter;

import com.example.cluvrapi.domain.gem.enums.GemActionType;
import com.example.cluvrapi.domain.gem.enums.GemUserActivityType;

@Getter
public class UpdateGemRequestDto {

	private Long userId;

	private Integer gem;

	private GemUserActivityType gemUserActivityType;

	public UpdateGemRequestDto(Long userId, Integer gem, GemUserActivityType gemUserActivityType) {
		this.userId = userId;
		this.gem = gem;
		this.gemUserActivityType = gemUserActivityType;
	}

	public static UpdateGemRequestDto from(Long userId, Integer gem, GemUserActivityType gemUserActivityType){
		return new UpdateGemRequestDto(userId, gem,gemUserActivityType);
	}
}
