package com.example.cluvrapi.domain.gem.dto.request;

import lombok.Getter;

import com.example.cluvrapi.domain.gem.dto.GemUpdateDto;
import com.example.cluvrapi.domain.gem.enums.GemUserActivityType;

@Getter
public class UseGemRequestDto implements GemUpdateDto {
	private Integer gem;
}

