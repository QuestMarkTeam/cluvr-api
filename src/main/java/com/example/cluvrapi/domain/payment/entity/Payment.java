package com.example.cluvrapi.domain.payment.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

// 결제 완료 로그 테이블
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String orderId;

	private String paymentKey;

	private Long userId;

	private int amount;

	private LocalDateTime paidAt;

	public Payment(String orderId, String paymentKey, Long userId, int amount, LocalDateTime paidAt) {
		this.orderId = orderId;
		this.paymentKey = paymentKey;
		this.userId = userId;
		this.amount = amount;
		this.paidAt = paidAt;
	}

}
