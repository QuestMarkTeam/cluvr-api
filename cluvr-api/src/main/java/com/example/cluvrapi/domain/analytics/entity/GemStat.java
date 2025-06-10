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
@Table(name = "gem_stat")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GemStat {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Integer gem;

	@Column(nullable = false)
	private Long userId;

	public GemStat(Integer gem, Long userId) {
		this.gem = gem;
		this.userId = userId;
	}
}
