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

import org.hibernate.annotations.SQLDelete;

import com.example.cluvrapi.domain.board.enums.BoardType;
import com.example.cluvrapi.domain.category.enums.CategoryType;
import com.example.cluvrapi.domain.common.entity.BaseTimeEntity;
import com.example.cluvrapi.domain.user.entity.User;

@Entity
@Getter
@Table(name = "boards")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE boards SET is_deleted = true WHERE id = ?")
public class Board extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private BoardType boardType;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private CategoryType category;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false)
	private String content;

	@Column(nullable = false, name = "is_selected")
	private boolean isSelected;

	@Column(nullable = false)
	private int clover;

	@Column(nullable = false, name = "is_deleted")
	private boolean isDeleted;

	@Column(nullable = false, name = "view_count")
	private int viewCount;

	public Board(User user, BoardType boardType, CategoryType category, String title, String content, int clover) {
		this.user = user;
		this.boardType = boardType;
		this.category = category;
		this.title = title;
		this.content = content;
		this.isSelected = false;
		this.clover = clover;
		this.isDeleted = false;
		this.viewCount = 0;
	}

	public void update(String title, String content, int clover) {
		this.title = title;
		this.content = content;
		this.clover = clover;
	}

}
