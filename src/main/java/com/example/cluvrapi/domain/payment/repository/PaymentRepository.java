package com.example.cluvrapi.domain.payment.repository;

import java.util.Optional;

import com.example.cluvrapi.domain.common.repository.BaseRepository;
import com.example.cluvrapi.domain.payment.dto.response.FindPaymentPrepareResponseDto;
import com.example.cluvrapi.domain.payment.entity.PaymentPending;
import com.example.cluvrapi.global.exception.BusinessException;
import com.example.cluvrapi.global.response.ResponseCode;

public interface PaymentRepository extends BaseRepository<PaymentPending,Long>,PaymentRepositoryQuery  {
	public Optional<PaymentPending> findPaymentPendingByOrderId(String orderId);

	default public PaymentPending findPaymentPrepareByOrderIdOrElseThrow(String orderId){
		return findPaymentPendingByOrderId(orderId).orElseThrow(()->new BusinessException(ResponseCode.PAYMENT_PREPARE_NOT_FOUND));
	}
}
