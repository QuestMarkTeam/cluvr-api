package com.example.cluvrapi.domain.reaction.repository;

import java.util.Map;
import java.util.Optional;

import jakarta.annotation.Nullable;

import com.example.cluvrapi.domain.board.entity.Board;
import com.example.cluvrapi.domain.reaction.entity.Reaction;
import com.example.cluvrapi.domain.reaction.enums.ReactionType;
import com.example.cluvrapi.domain.reply.entity.Reply;
import com.example.cluvrapi.domain.user.entity.User;

public interface ReactionRepositoryCustom {
	Optional<Reaction> findReaction(User user, Board board, @Nullable Reply reply);

	long countBoardReactions(long boardId, ReactionType reactionType);

	long countReplyReactions(long replyId, ReactionType reactionType);
}
