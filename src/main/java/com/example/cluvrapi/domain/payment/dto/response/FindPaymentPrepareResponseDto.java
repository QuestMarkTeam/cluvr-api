package com.example.cluvrapi.domain.payment.dto.response;

import lombok.Getter;

@Getter
public class FindPaymentPrepareResponseDto {
	private Integer amount;

	private Long userId;

	private String orderId;

	public FindPaymentPrepareResponseDto(Integer amount, Long userId, String orderId) {
		this.amount = amount;
		this.userId = userId;
		this.orderId = orderId;
	}

	public static FindPaymentPrepareResponseDto of(Integer amount, Long userId, String orderId) {
		return new FindPaymentPrepareResponseDto(amount, userId, orderId);
	}
}
