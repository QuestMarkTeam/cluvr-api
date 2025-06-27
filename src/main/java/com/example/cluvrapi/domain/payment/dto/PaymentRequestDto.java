package com.example.cluvrapi.domain.payment.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Getter;

@Getter
public class PaymentRequestDto {

	@NotBlank(message = "결제키는 필수 입니다.")
	private String paymentKey;

	@NotNull(message = "결제 금액은 필수입니다.")
	@Min(value = 1, message = "결제 금액은 1 이상이어야 합니다.")
	private Integer amount;
}
