package com.example.cluvrapi.domain.point.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import lombok.Getter;

@Getter
public class CreatePointRequestDto {
	@NotNull(message = "금액을 입력해야 합니다.")
	@Min(value = 0, message = "0원 이상의 값을 입력해야 합니다.")
	private Integer amount;
}
