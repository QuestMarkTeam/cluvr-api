package com.example.cluvrapi.domain.reply.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.example.cluvrapi.domain.board.entity.Board;
import com.example.cluvrapi.domain.reply.entity.Reply;
import com.example.cluvrapi.domain.user.entity.User;

@Getter
@AllArgsConstructor
public class CreateReplyRequestDto {
	@NotBlank(message = "댓글 내용이 비어있습니다.")
	@Size(min = 1, max = 255, message = "댓글 내용은 10자에서 255자 사이로 작성하셔야 합니다.")
	private String content;

	@Positive(message = "parent의 Id는 항상 양수입니다.")
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
