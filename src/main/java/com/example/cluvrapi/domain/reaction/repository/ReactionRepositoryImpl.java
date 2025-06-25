package com.example.cluvrapi.domain.reaction.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.example.cluvrapi.domain.board.entity.Board;
import com.example.cluvrapi.domain.reaction.entity.QReaction;
import com.example.cluvrapi.domain.reaction.entity.Reaction;
import com.example.cluvrapi.domain.reaction.enums.ReactionType;
import com.example.cluvrapi.domain.reply.entity.Reply;
import com.example.cluvrapi.domain.user.entity.User;
import com.querydsl.core.Tuple;
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
	public Map<ReactionType, Long> countBoardReactions(Board board) {
		QReaction reaction = QReaction.reaction;

		List<Tuple> result = queryFactory
			.select(reaction.reactionType, reaction.count())
			.from(reaction)
			.where(reaction.board.eq(board))
			.groupBy(reaction.reactionType)
			.fetch();

		return result.stream().collect(Collectors.toMap(
			tuple -> tuple.get(reaction.reactionType),
			tuple -> tuple.get(reaction.count())
		));
	}

}
