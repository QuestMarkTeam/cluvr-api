package com.example.cluvrapi.domain.payment.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.cluvrapi.domain.common.annotation.Auth;
import com.example.cluvrapi.domain.common.dto.AuthUser;
import com.example.cluvrapi.domain.payment.dto.request.PaymentRequestDto;
import com.example.cluvrapi.domain.payment.service.PaymentService;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

	private final PaymentService paymentService;

	@PostMapping("/confirm")
	public void confirmPayment(
		@RequestParam PaymentRequestDto paymentRequestDto,
		@Auth AuthUser authUser
	){
		paymentService.confirmPayment(authUser.id(), paymentRequestDto);
	}
}
