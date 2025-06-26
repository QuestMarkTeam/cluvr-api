package com.example.cluvrapi.domain.gem.dto.request;


import lombok.Getter;

import com.example.cluvrapi.domain.gem.enums.GemActionType;
import com.example.cluvrapi.domain.gem.enums.GemUserActivityType;

@Getter
public class UpdateGemRequestDto {

	private Integer gem;

	private GemUserActivityType gemUserActivityType;

	public UpdateGemRequestDto(Integer gem, GemUserActivityType gemUserActivityType) {
		this.gem = gem;
		this.gemUserActivityType = gemUserActivityType;
	}

	public static UpdateGemRequestDto from(Integer gem, GemUserActivityType gemUserActivityType){
		return new UpdateGemRequestDto(gem,gemUserActivityType);
	}
}
