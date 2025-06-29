package com.example.cluvrapi.domain.payment.repository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Repository;

import com.example.cluvrapi.domain.payment.dto.response.FindPaymentPrepareResponseDto;
import com.querydsl.jpa.impl.JPAQueryFactory;

@RequiredArgsConstructor
public class PaymentRepositoryQueryImpl implements PaymentRepositoryQuery{

	private final JPAQueryFactory jpaQueryFactory;
}
