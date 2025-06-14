package com.example.cluvrapi.domain.reaction.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.example.cluvrapi.domain.board.enums.ReactionType;

@Getter
@AllArgsConstructor
public class ReactionRequestDto {
	private ReactionType reactionType;

	private Long boardId;

	private Long replyId;
}
