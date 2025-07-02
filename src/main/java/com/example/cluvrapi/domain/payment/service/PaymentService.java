package com.example.cluvrapi.domain.payment.service;

import com.example.cluvrapi.domain.payment.dto.PaymentConfirmResponseDto;
import com.example.cluvrapi.domain.payment.dto.request.PaymentConfirmRequestDto;
import com.example.cluvrapi.domain.payment.dto.response.CreatePaymentPrepareResponseDto;
import com.example.cluvrapi.domain.payment.dto.response.FindPaymentPrepareResponseDto;
import com.example.cluvrapi.domain.payment.dto.request.CreatePaymentPrepareRequestDto;

public interface PaymentService {

	/**
	 * 설명: {메서드에 대한 간략한 설명을 작성합니다.}
	 * <p>
	 * 사용자가 결제하기 클릭하면 서버에 가격,유저 아이디, 주문번호 생성해서 저장
	 *
	 * @author {작성자 이름}
	 */

	CreatePaymentPrepareResponseDto savePaymentPendingInfo(Long userId, CreatePaymentPrepareRequestDto requestDto);

	FindPaymentPrepareResponseDto findPaymentPending(Long userId, String orderId);

	PaymentConfirmResponseDto confirmPayment(Long userId, PaymentConfirmRequestDto requestDto);
}
