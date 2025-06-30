package com.example.cluvrapi.domain.payment.dto.request;

import lombok.Getter;

@Getter
public class PaymentConfirmRequestDto {
	private String paymentKey;
	private String orderId;
	private Integer amount;
}
