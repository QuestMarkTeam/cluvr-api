package com.example.cluvrapi.domain.board.entity;

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

import com.example.cluvrapi.domain.board.enums.ReactionType;
import com.example.cluvrapi.domain.user.entity.User;

@Getter
@Entity
@Table(name = "board_reactions")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardReaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user", nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "board", nullable = false)
	private Board board;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ReactionType type;

	public BoardReaction(User user, Board board, ReactionType type) {
		this.user = user;
		this.board = board;
		this.type = type;
	}
}
