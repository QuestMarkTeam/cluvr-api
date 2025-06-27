package com.example.cluvrapi.domain.payment.service;

import lombok.extern.slf4j.Slf4j;

import org.apache.juli.logging.Log;
import org.springframework.stereotype.Service;

import com.example.cluvrapi.domain.payment.dto.PaymentRequestDto;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService{

	@Override
	public void confirmPayment(Long id, PaymentRequestDto paymentRequestDto) {
		log.info("paymentRequestDto -> {} ",paymentRequestDto);
	}

	@Override
	public void savePaymentInfo() {

	}
}
