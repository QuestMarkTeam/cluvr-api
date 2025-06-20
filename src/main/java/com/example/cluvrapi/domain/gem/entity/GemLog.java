package com.example.cluvrapi.domain.gem.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.example.cluvrapi.domain.gem.enums.GemActionType;
import com.example.cluvrapi.domain.user.entity.User;

@Entity
@Getter
@Table(name = "gem_logs")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GemLog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Column(nullable = false)
	private String description;

	@Column(nullable = false)
	private Integer amount;

	@Column(nullable = false)
	private LocalDateTime createdAt;

	private LocalDateTime deletedAt;

	@Enumerated(EnumType.STRING)
	@Column(name = "action_type", nullable = false)
	private GemActionType actionType;

	public GemLog(User user, String description, Integer amount, LocalDateTime createdAt,
		LocalDateTime deletedAt,
		GemActionType actionType) {
		this.user = user;
		this.description = description;
		this.amount = amount;
		this.createdAt = createdAt;
		this.deletedAt = deletedAt;
		this.actionType = actionType;
	}

}
