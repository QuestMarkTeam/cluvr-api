package com.example.cluvrapi.domain.payment.service;

import com.example.cluvrapi.domain.payment.dto.response.FindPaymentPrepareResponseDto;
import com.example.cluvrapi.domain.payment.dto.request.PaymentPrepareRequestDto;
import com.example.cluvrapi.domain.payment.dto.request.PaymentRequestDto;
import com.example.cluvrapi.domain.payment.dto.response.PaymentPrepareResponseDto;

public interface PaymentService {
	void confirmPayment(Long id, PaymentRequestDto paymentRequestDto);

	/**
	 * 설명: {메서드에 대한 간략한 설명을 작성합니다.}
	 *
	 * 사용자가 결제하기 클릭하면 서버에 가격,유저 아이디, 주문번호 생성해서 저장
	 *
	 * @return {반환값에 대한 설명}
	 *
	 * @author {작성자 이름}
	 */

	PaymentPrepareResponseDto savePaymentInfo(Long userId, PaymentPrepareRequestDto requestDto);

	FindPaymentPrepareResponseDto findPaymentPending(Long id, String orderId);
}
