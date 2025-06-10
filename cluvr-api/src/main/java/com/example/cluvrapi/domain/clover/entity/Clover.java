package com.example.cluvrapi.domain.clover.entity;

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

import com.example.cluvrapi.domain.clover.eunms.Tier;
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

	@OneToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	public Clover(Tier tier, Integer score, User user) {
		this.tier = tier;
		this.score = score;
		this.user = user;
	}

	public void updateScore(Integer score) {
		this.score = score;
	}
}
