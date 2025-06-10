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
@Table(name = "category_stat")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CategoryStat {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Integer totalAnswer;

	private Integer totalSelected;

	@Column(nullable = false)
	private Integer totalScore;

	private Integer totalQuestion;

	@Column(nullable = false)
	private Long userId;

	@Column(nullable = false)
	private Long categoryId;

	public CategoryStat(Integer totalAnswer, Integer totalSelected, Integer totalScore,
		Integer totalQuestion, Long userId, Long categoryId) {
		this.totalAnswer = totalAnswer;
		this.totalSelected = totalSelected;
		this.totalScore = totalScore;
		this.totalQuestion = totalQuestion;
		this.userId = userId;
		this.categoryId = categoryId;
	}

}
