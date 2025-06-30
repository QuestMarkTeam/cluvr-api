package com.example.cluvrapi.domain.payment.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "payment_pending")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentPending {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String orderId; // 고유 주문번호

	@Column(nullable = false)
	private Long userId;

	@Column(nullable = false)
	private String uuid;

	@Column(nullable = false)
	private int finalAmount; // 최종 결제 금액

	private String orderName;

	@Column(nullable = false)
	private LocalDateTime createdAt;

	@Column(nullable = false)
	private LocalDateTime expiresAt;

	@Column(nullable = false)
	private boolean committed; // 결제 완료 여부 (true = 확정됨)

	public void markCommitted() {
		this.committed = true;
	}

	public PaymentPending(String orderId, Long userId, String uuid, int finalAmount, String orderName,
		LocalDateTime createdAt, LocalDateTime expiresAt, boolean committed) {
		this.orderId = orderId;
		this.userId = userId;
		this.uuid = uuid;
		this.finalAmount = finalAmount;
		this.orderName = orderName;
		this.createdAt = createdAt;
		this.expiresAt = expiresAt;
		this.committed = committed;
	}

	public void updateCommit(){
		this.committed = true;
	}

	public boolean isExpired() {
		return LocalDateTime.now().isAfter(this.expiresAt);
	}
}
