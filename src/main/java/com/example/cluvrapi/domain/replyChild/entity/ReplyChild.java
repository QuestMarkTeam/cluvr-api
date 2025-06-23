package com.example.cluvrapi.domain.replyChild.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
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
import com.example.cluvrapi.domain.reply.entity.Reply;
import com.example.cluvrapi.domain.user.entity.User;

@Entity
@Table(name = "reply_children")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReplyChild extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "reply_id")
	private Reply parent;

	@Column(nullable = false)
	private String content;

	@Column(nullable = false, name = "is_deleted")
	private boolean isDeleted;

	@Embedded
	private MentionInfo mention;

	public ReplyChild(User user, String content, Reply parent, MentionInfo mentionInfo) {
		this.user = user;
		this.parent = parent;
		this.content = content;
		this.isDeleted = false;
		this.mention = mentionInfo;
	}

	public void update(MentionInfo mentionInfo, String content) {
		this.mention = mentionInfo;
		this.content = content;
	}

}
