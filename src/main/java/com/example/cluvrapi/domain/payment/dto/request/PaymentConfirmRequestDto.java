package com.example.cluvrapi.domain.payment.dto.request;

import lombok.Getter;

import com.example.cluvrapi.domain.gem.dto.GemUpdateDto;

@Getter
public class PaymentConfirmRequestDto implements GemUpdateDto {
	private String paymentKey;
	private String orderId;
	private Integer gem;

}
