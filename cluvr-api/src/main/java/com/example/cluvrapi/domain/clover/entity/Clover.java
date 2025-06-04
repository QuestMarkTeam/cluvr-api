package com.example.cluvrapi.domain.clover.entity;

import com.example.cluvrapi.domain.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.example.cluvrapi.domain.clover.enums.Tier;
import com.example.cluvrapi.domain.user.entity.User;

@Entity
@Getter
@Table(name = "clovers")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Clover {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	private Tier tier;

	private Integer score;

	@Column(nullable = false)
	private Long userId;

	public Clover(Tier tier, Integer score, User user) {
		this.tier = tier;
		this.score = score;
		this.user = user;
	}

	public void updateScore(Integer score) {
		this.score = score;
	}
}
