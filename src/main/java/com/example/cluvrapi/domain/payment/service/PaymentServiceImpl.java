package com.example.cluvrapi.domain.payment.service;

import lombok.extern.slf4j.Slf4j;

import org.apache.juli.logging.Log;
import org.springframework.stereotype.Service;

import com.example.cluvrapi.domain.payment.dto.request.PaymentPrepareRequestDto;
import com.example.cluvrapi.domain.payment.dto.request.PaymentRequestDto;
import com.example.cluvrapi.domain.payment.dto.response.PaymentPrepareResponseDto;
import com.example.cluvrapi.domain.payment.util.OrderIdGenerator;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService{

	@Override
	public void confirmPayment(Long id, PaymentRequestDto paymentRequestDto) {
		log.info("paymentRequestDto -> {} ",paymentRequestDto);
	}

	@Override
	public PaymentPrepareResponseDto savePaymentInfo(Long userId, PaymentPrepareRequestDto requestDto) {
		log.info("requestDto -> {} ",requestDto);
		String orderId = OrderIdGenerator.generate();
		return PaymentPrepareResponseDto.of(requestDto.getAmount(),requestDto.getOrderName(), orderId);
	}
}
