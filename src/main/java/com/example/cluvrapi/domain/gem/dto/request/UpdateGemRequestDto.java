package com.example.cluvrapi.domain.gem.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import lombok.Getter;

@Getter
public class UpdateGemRequestDto {
	@NotNull(message = "금액을 입력해야 합니다.")
	@Min(value = 1, message = "1 이상의 값을 입력해야 합니다.")
	private Integer amount;
}
