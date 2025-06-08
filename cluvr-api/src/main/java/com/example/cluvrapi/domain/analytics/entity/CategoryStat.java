package com.example.cluvrapi.domain.analytics.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Getter;

@Entity
@Getter
@Table(name = "category_stat")
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

}
