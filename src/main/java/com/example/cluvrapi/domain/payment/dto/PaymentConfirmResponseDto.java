package com.example.cluvrapi.domain.payment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import com.example.cluvrapi.domain.gem.dto.response.UpdateGemResponseDto;

@Getter
@NoArgsConstructor
public class PaymentConfirmResponseDto {
	private String paymentKey;
	private String orderId;
	private Integer amount;

	public PaymentConfirmResponseDto(String paymentKey, String orderId, Integer amount) {
		this.paymentKey = paymentKey;
		this.orderId = orderId;
		this.amount = amount;
	}

	public static PaymentConfirmResponseDto from(String paymentKey, String orderId, Integer amount) {
		return new PaymentConfirmResponseDto(paymentKey, orderId, amount);
	}
}
