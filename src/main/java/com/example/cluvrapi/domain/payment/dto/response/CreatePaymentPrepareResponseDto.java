package com.example.cluvrapi.domain.payment.dto.response;

import lombok.Getter;

@Getter
public class CreatePaymentPrepareResponseDto {

	private String uuid;

	private Integer amount;

	private String orderName;

	private String orderId;

	public CreatePaymentPrepareResponseDto(String uuid, Integer amount, String orderName, String orderId) {
		this.uuid = uuid;
		this.amount = amount;
		this.orderName = orderName;
		this.orderId = orderId;
	}

	public static CreatePaymentPrepareResponseDto of(String uuid, Integer amount, String orderName, String orderId) {
		return new CreatePaymentPrepareResponseDto(uuid, amount, orderName, orderId);
	}

}
