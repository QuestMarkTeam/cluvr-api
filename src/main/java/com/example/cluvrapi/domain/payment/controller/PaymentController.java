package com.example.cluvrapi.domain.payment.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.cluvrapi.domain.common.annotation.Auth;
import com.example.cluvrapi.domain.common.dto.AuthUser;
import com.example.cluvrapi.domain.payment.dto.response.FindPaymentPrepareResponseDto;
import com.example.cluvrapi.domain.payment.dto.request.PaymentPrepareRequestDto;
import com.example.cluvrapi.domain.payment.dto.request.PaymentRequestDto;
import com.example.cluvrapi.domain.payment.dto.response.PaymentPrepareResponseDto;
import com.example.cluvrapi.domain.payment.service.PaymentService;
import com.example.cluvrapi.global.response.BaseResponse;
import com.example.cluvrapi.global.response.ResponseCode;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

	private final PaymentService paymentService;

	@PostMapping("/confirm")
	public void confirmPayment(
		@RequestParam PaymentRequestDto paymentRequestDto,
		@Auth AuthUser authUser
	) {
		paymentService.confirmPayment(authUser.id(), paymentRequestDto);
	}

	/**
	 * 설명: 결제 UI 호출
	 * 클라이언트로 가격, 결제정보 넘겨 받음 결제
	 * 처리 후 결제페이지로 이동함, 결제 상태 서버에 저장
	 * @return {반환값에 대한 설명}
	 *
	 * @author {작성자 이름}
	 */
	@PostMapping("/checkout")
	public ResponseEntity<BaseResponse<PaymentPrepareResponseDto>> preparePayment(
		@RequestBody PaymentPrepareRequestDto requestDto,
		@Auth AuthUser authUser
	) {
		PaymentPrepareResponseDto responseDto = paymentService.savePaymentInfo(authUser.id(), requestDto);
		return ResponseEntity.ok(BaseResponse.success(responseDto, ResponseCode.OK));
	}



	/**
	 * 설명: 결제 정보 클라이언트로 전송
	 *
	 * @return {반환값에 대한 설명}
	 *
	 * @author {작성자 이름}
	 */
	@GetMapping("/{orderId}")
	public ResponseEntity<BaseResponse<FindPaymentPrepareResponseDto>> findPaymentPending(
		@PathVariable String orderId,
		@Auth AuthUser authUser
	) {
		FindPaymentPrepareResponseDto responseDto = paymentService.findPaymentPending(authUser.id(), orderId);
		return ResponseEntity.ok(BaseResponse.success(responseDto, ResponseCode.OK));
	}
}
