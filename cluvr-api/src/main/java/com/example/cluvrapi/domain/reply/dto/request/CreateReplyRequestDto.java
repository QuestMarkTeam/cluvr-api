package com.example.cluvrapi.domain.reply.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.example.cluvrapi.domain.board.entity.Board;
import com.example.cluvrapi.domain.reply.entity.Reply;
import com.example.cluvrapi.domain.user.entity.User;

@Getter
@AllArgsConstructor
public class CreateReplyRequestDto {
	private String content;
	private Long parentId;

	public Reply fromDto(User user, Board board, Reply parent) {
		return new Reply(
			user,
			content,
			board,
			parent
		);
	}
}
