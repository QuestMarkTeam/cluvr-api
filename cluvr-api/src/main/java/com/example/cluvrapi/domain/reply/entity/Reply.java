package com.example.cluvrapi.domain.reply.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

import com.example.cluvrapi.domain.board.entity.Board;
import com.example.cluvrapi.domain.common.entity.BaseTimeEntity;
import com.example.cluvrapi.domain.user.entity.User;

@Entity
@Table(name = "replies")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reply extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id")
	private Reply parent;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "board_id")
	private Board board;

	@Column(nullable = false)
	private String content;

	@Column(nullable = false, name = "is_deleted")
	private boolean isDeleted;

	@Column(nullable = false, name = "is_selected")
	private boolean isSelected;

	public Reply(User user, String content, Board board, Reply parent) {
		this.user = user;
		this.content = content;
		this.board = board;
		this.parent = parent;
		this.isDeleted = false;
		this.isSelected = false;
	}

	public void update(String content) {
		this.content = content;
	}

	public void updateSelection() {
		this.isSelected = true;
	}

	public void delete() {
		this.isDeleted = true;
	}
}
