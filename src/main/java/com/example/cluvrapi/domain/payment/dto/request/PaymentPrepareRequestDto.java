package com.example.cluvrapi.domain.payment.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Getter;

@Getter
public class PaymentPrepareRequestDto {
	@NotNull(message = "결제 금액은 필수입니다.")
	@Min(value = 1, message = "결제 금액은 1 이상이어야 합니다.")
	private Integer amount;

	@NotBlank(message = "결제 정보는 필수입니다")
	private String orderName;
}
