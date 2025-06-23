package com.example.cluvrapi.domain.replyChild.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MentionInfo {
	@Column(name = "mentioned_user_id")
	private Long userId;

	@Column(name = "mentioned_user_name")
	private String userName;
}
