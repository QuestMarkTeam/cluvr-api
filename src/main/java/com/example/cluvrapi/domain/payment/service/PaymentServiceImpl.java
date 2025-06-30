package com.example.cluvrapi.domain.payment.service;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.cluvrapi.domain.gem.enums.GemUserActivityType;
import com.example.cluvrapi.domain.payment.dto.PaymentConfirmResponseDto;
import com.example.cluvrapi.domain.payment.dto.request.PaymentConfirmRequestDto;
import com.example.cluvrapi.domain.payment.dto.response.CreatePaymentPrepareResponseDto;
import com.example.cluvrapi.domain.payment.dto.response.CreatePaymentResponseDto;
import com.example.cluvrapi.domain.payment.dto.response.FindPaymentPrepareResponseDto;
import com.example.cluvrapi.domain.payment.dto.request.CreatePaymentPrepareRequestDto;
import com.example.cluvrapi.domain.payment.dto.request.PaymentRequestDto;
import com.example.cluvrapi.domain.payment.dto.response.PaymentPrepareResponseDto;
import com.example.cluvrapi.domain.payment.entity.Payment;
import com.example.cluvrapi.domain.payment.entity.PaymentPending;
import com.example.cluvrapi.domain.payment.repository.PaymentPendingRepository;
import com.example.cluvrapi.domain.payment.repository.PaymentRepository;
import com.example.cluvrapi.domain.payment.util.OrderIdGenerator;
import com.example.cluvrapi.global.annotation.UpdateGem;
import com.example.cluvrapi.global.exception.BusinessException;
import com.example.cluvrapi.global.response.ResponseCode;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService{

	private final PaymentPendingRepository paymentPendingRepository;
	private final PaymentRepository paymentRepository;

	@Value("${TOSS_SECRET_KEY}")
	private String secretKey;


	@Transactional
	@Override
	public CreatePaymentPrepareResponseDto savePaymentPendingInfo(Long userId, CreatePaymentPrepareRequestDto requestDto) {
		String orderId = OrderIdGenerator.generate();
		String uuid = UUID.randomUUID().toString();
		PaymentPending paymentPending = new PaymentPending(
			orderId,
			userId,
			uuid,
			requestDto.getAmount(),
			requestDto.getOrderName(),
			LocalDateTime.now(),
			LocalDateTime.now().plusMinutes(15),
			false
		);
		paymentPendingRepository.save(paymentPending);
		return CreatePaymentPrepareResponseDto.of(uuid, requestDto.getAmount(),requestDto.getOrderName(), orderId);
	}

	@Override
	public FindPaymentPrepareResponseDto findPaymentPending(Long userId, String orderId) {
		PaymentPending paymentPending = paymentPendingRepository.findPaymentPrepareByOrderIdOrElseThrow(orderId);
		return FindPaymentPrepareResponseDto.of(
			paymentPending.getFinalAmount(),
			paymentPending.getUserId(),
			paymentPending.getOrderId()
		);
	}

	@Transactional
	@UpdateGem(value = GemUserActivityType.CHARGE)
	@Override
	public PaymentConfirmResponseDto confirmPayment(Long userId, PaymentConfirmRequestDto requestDto) {
		String orderId = requestDto.getOrderId();
		String paymentKey = requestDto.getPaymentKey();
		Integer amount = requestDto.getGem();
		PaymentPending paymentPending = paymentPendingRepository.findPaymentPrepareByOrderIdOrElseThrow(orderId);
		if(paymentPending.getFinalAmount() != amount) {throw new BusinessException(ResponseCode.PAYMENT_AMOUNT_FAILED);}
		paymentPending.updateCommit();
		// 결제 정보 저장
		Payment payment = new Payment(
			orderId,
			paymentKey,
			userId,
			amount,
			LocalDateTime.now()
		);
		paymentRepository.save(payment);

		// 토스에 결제 승인 보내기
		String encodedKey = Base64.getEncoder().encodeToString((secretKey + ":").getBytes());
		WebClient webClient = WebClient.builder()
			.baseUrl("https://api.tosspayments.com/v1")
			.defaultHeader(HttpHeaders.AUTHORIZATION, "Basic " + encodedKey)
			.build();
		return webClient.post()
			.uri("/payments/confirm")
			.bodyValue(Map.of(
				"paymentKey",paymentKey,
				"orderId", orderId,
				"amount", amount
			))
			.retrieve()
			.bodyToMono(PaymentConfirmResponseDto.class)
			.block();
	}

}
