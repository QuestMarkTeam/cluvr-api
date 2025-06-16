package com.example.cluvrapi.domain.reaction.dto.request;

import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;

import com.example.cluvrapi.domain.reaction.enums.ReactionType;

@Getter
@NoArgsConstructor
public class ReactionRequestDto {
	@NotNull(message = "reactionType은 필수입니다.")
	private ReactionType reactionType;

	@NotNull(message = "boardId는 필수입니다.")
	private Long boardId;

	private Long replyId;
}
