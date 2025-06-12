package com.example.cluvrapi.domain.analytics.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "user_stat")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserBoardStat {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Integer score;

	@Column(nullable = false)
	private Long userId;

	@Column(nullable = false)
	private Long categoryId;

	public UserBoardStat(Integer score, Long categoryId, Long userId) {
		this.score = score;
		this.categoryId = categoryId;
		this.userId = userId;
	}
}
