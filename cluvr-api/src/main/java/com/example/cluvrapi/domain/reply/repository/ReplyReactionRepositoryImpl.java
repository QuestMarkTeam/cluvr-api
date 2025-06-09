package com.example.cluvrapi.domain.reply.repository;

import jakarta.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import com.example.cluvrapi.domain.reply.entity.QReplyReactions;
import com.example.cluvrapi.domain.reply.entity.Reply;
import com.example.cluvrapi.domain.user.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class ReplyReactionRepositoryImpl implements ReplyReactionRepositoryCustom {
	JPAQueryFactory queryFactory;

	public ReplyReactionRepositoryImpl(EntityManager entityManager) {
		this.queryFactory = new JPAQueryFactory(entityManager);
	}

	@Override
	public void deleteByUserAndReply(User user, Reply reply) {
		QReplyReactions replyReaction = QReplyReactions.replyReactions;

		queryFactory
			.delete(replyReaction)
			.where(
				replyReaction.user.eq(user),
				replyReaction.reply.eq(reply)
			).execute();
	}
}
