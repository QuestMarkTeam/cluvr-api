package com.example.cluvrapi.domain.analytics.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.example.cluvrapi.domain.clover.enums.Tier;

@Entity
@Getter
@Table(name = "user_stat")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserBoardStat {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Long userId;

	@Column(nullable = false)
	private Integer totalAnswer;

	@Column(nullable = false)
	private Integer totalSelected;

	@Column(nullable = false)
	private Integer totalQuestion;

	@Column(nullable = false)
	private Integer totalClover;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Tier tier;

	public UserBoardStat(Long id, Long userId, Integer totalAnswer, Integer totalSelected, Integer totalQuestion,
		Integer totalClover, Tier tier) {
		this.id = id;
		this.userId = userId;
		this.totalAnswer = totalAnswer;
		this.totalSelected = totalSelected;
		this.totalQuestion = totalQuestion;
		this.totalClover = totalClover;
		this.tier = tier;
	}

	public void updateStats(Integer answer, Integer selected, Integer question, Integer clover,
		Tier tier) {
		this.totalAnswer = answer;
		this.totalSelected = selected;
		this.totalQuestion = question;
		this.totalClover = clover;
		this.tier = tier;
	}

}
