package com.example.cluvrapi.domain.payment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import com.example.cluvrapi.domain.payment.dto.response.FindPaymentPrepareResponseDto;
import com.example.cluvrapi.domain.payment.dto.request.PaymentPrepareRequestDto;
import com.example.cluvrapi.domain.payment.dto.request.PaymentRequestDto;
import com.example.cluvrapi.domain.payment.dto.response.PaymentPrepareResponseDto;
import com.example.cluvrapi.domain.payment.entity.PaymentPending;
import com.example.cluvrapi.domain.payment.repository.PaymentRepository;
import com.example.cluvrapi.domain.payment.util.OrderIdGenerator;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService{

	private final PaymentRepository paymentRepository;

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

	@Override
	public FindPaymentPrepareResponseDto findPaymentPending(Long id, String orderId) {
		PaymentPending paymentPending = paymentRepository.findPaymentPrepareByOrderIdOrElseThrow(orderId);
		return FindPaymentPrepareResponseDto.of(
				paymentPending.getFinalAmount(),
				paymentPending.getUserId(),
				paymentPending.getOrderId()
			);
	}
}
