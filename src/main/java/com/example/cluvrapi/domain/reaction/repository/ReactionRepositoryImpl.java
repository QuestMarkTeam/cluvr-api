package com.example.cluvrapi.domain.reaction.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.cluvrapi.domain.board.entity.Board;
import com.example.cluvrapi.domain.reaction.entity.QReaction;
import com.example.cluvrapi.domain.reaction.entity.Reaction;
import com.example.cluvrapi.domain.reaction.enums.ReactionType;
import com.example.cluvrapi.domain.reply.entity.Reply;
import com.example.cluvrapi.domain.user.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class ReactionRepositoryImpl implements ReactionRepositoryCustom {
	private final JPAQueryFactory queryFactory;

	public ReactionRepositoryImpl(JPAQueryFactory queryFactory) {
		this.queryFactory = queryFactory;
	}

	@Override
	public Optional<Reaction> findReaction(User user, Board board, Reply reply) {
		QReaction reaction = QReaction.reaction;

		return Optional.ofNullable(
			queryFactory
				.selectFrom(reaction)
				.where(
					reaction.user.eq(user),
					reaction.board.eq(board),
					reply == null ? reaction.reply.isNull() : reaction.reply.eq(reply)
				)
				.fetchOne()
		);
	}

	@Override
	public long countBoardReactions(long boardId, ReactionType reactionType) {
		QReaction reaction = QReaction.reaction;

		Long count =  queryFactory
			.select(reaction.count())
			.from(reaction)
			.where(reaction.board.id.eq(boardId).and(reaction.reactionType.eq(reactionType)))
			.groupBy(reaction.reactionType)
			.fetchOne();
		return count != null ? count : 0;
	}

	@Override
	public long countReplyReactions(long replyId, ReactionType reactionType) {
		QReaction reaction = QReaction.reaction;

		Long count =  queryFactory
			.select(reaction.count())
			.from(reaction)
			.where(reaction.reply.id.eq(replyId).and(reaction.reactionType.eq(reactionType)))
			.groupBy(reaction.reactionType)
			.fetchOne();
		return count != null ? count : 0;
	}
}
