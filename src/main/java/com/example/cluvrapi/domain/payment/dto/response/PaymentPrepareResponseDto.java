package com.example.cluvrapi.domain.payment.dto.response;

import lombok.Getter;

@Getter
public class PaymentPrepareResponseDto {

	private Integer amount;

	private String orderName;

	private String orderId;

	public PaymentPrepareResponseDto(Integer amount, String orderName, String orderId) {
		this.amount = amount;
		this.orderName = orderName;
		this.orderId = orderId;
	}

	public static PaymentPrepareResponseDto of(Integer amount, String orderName, String orderId) {
		return new PaymentPrepareResponseDto(amount, orderName, orderId);
	}
}
