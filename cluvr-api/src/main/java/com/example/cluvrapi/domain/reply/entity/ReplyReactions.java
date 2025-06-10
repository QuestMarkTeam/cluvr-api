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
import jakarta.persistence.UniqueConstraint;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.example.cluvrapi.domain.board.enums.ReactionType;
import com.example.cluvrapi.domain.user.entity.User;

@Entity
@Table(name = "reply_reactions", uniqueConstraints = {
	@UniqueConstraint(columnNames = {"user_id", "reply_id"})
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReplyReactions {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reply_id", nullable = false)
	private Reply reply;

	@Column(nullable = false)
	private ReactionType type;

	public ReplyReactions(User user, Reply reply, ReactionType type) {
		this.user = user;
		this.reply = reply;
		this.type = type;
	}

}
