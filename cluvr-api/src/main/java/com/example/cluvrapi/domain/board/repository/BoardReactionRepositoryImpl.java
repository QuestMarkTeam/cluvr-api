package com.example.cluvrapi.domain.board.repository;

import jakarta.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import com.example.cluvrapi.domain.board.entity.Board;
import com.example.cluvrapi.domain.board.entity.QBoardReaction;
import com.example.cluvrapi.domain.user.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class BoardReactionRepositoryImpl implements BoardReactionRepositoryCustom {
	JPAQueryFactory queryFactory;

	public BoardReactionRepositoryImpl(EntityManager entityManager) {
		this.queryFactory = new JPAQueryFactory(entityManager);
	}

	@Override
	public void deleteByUserAndBoard(User user, Board board) {
		QBoardReaction boardReaction = QBoardReaction.boardReaction;

		queryFactory
			.delete(boardReaction)
			.where(
				boardReaction.user.eq(user),
				boardReaction.board.eq(board)
			).execute();
	}
}
