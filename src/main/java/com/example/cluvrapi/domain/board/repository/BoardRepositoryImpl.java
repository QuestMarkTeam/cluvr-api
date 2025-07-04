package com.example.cluvrapi.domain.board.repository;

import java.util.List;

import jakarta.persistence.EntityManager;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.example.cluvrapi.domain.board.dto.response.QReadMyBoardsResponseDto;
import com.example.cluvrapi.domain.board.dto.response.ReadMyBoardsResponseDto;
import com.example.cluvrapi.domain.board.entity.Board;
import com.example.cluvrapi.domain.board.entity.QBoard;
import com.example.cluvrapi.domain.board.enums.BoardType;
import com.example.cluvrapi.domain.category.enums.CategoryType;
import com.example.cluvrapi.domain.common.dto.PageResponseDto;
import com.example.cluvrapi.domain.user.entity.QUser;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class BoardRepositoryImpl implements BoardRepositoryCustom {
	JPAQueryFactory queryFactory;

	public BoardRepositoryImpl(EntityManager entityManager) {
		this.queryFactory = new JPAQueryFactory(entityManager);
	}

	@Override
	public Board findBoardById(long id) {
		QBoard board = QBoard.board;
		QUser user = QUser.user;

		return queryFactory
			.selectFrom(board)
			.leftJoin(board.user, user).fetchJoin()
			.where(
				board.id.eq(id)
			)
			.fetchOne();
	}

	@Override
	public PageResponseDto<Board> findAllBoardsByCategory(CategoryType category, BoardType boardType, Pageable pageable) {
		QBoard board = QBoard.board;

		List<Board> dtos = queryFactory
			.selectFrom(board)
			.where(
				board.isDeleted.isFalse()
					.and(category != null ? board.category.eq(category) : null)
					.and(board.boardType.eq(boardType))
			)
			.orderBy(board.createdAt.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long total = queryFactory
			.select(board.count())
			.from(board)
			.where(
				board.isDeleted.isFalse()
					.and(category != null ? board.category.eq(category) : null)
					.and(board.boardType.eq(boardType))
			)
			.fetchOne();

		return PageResponseDto.toDto(new PageImpl<>(dtos, pageable, total));
	}

	@Override
	public PageResponseDto<ReadMyBoardsResponseDto> findBoardsByUser(long userId, Pageable pageable) {
		QBoard board = QBoard.board;

		List<ReadMyBoardsResponseDto> dtos = queryFactory
			.select(new QReadMyBoardsResponseDto(board.id, board.title, board.content, board.createdAt))
			.from(board)
			.where(board.user.id.eq(userId))
			.orderBy(board.createdAt.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long total = queryFactory
			.select(board.count())
			.from(board)
			.where(board.user.id.eq(userId))
			.fetchOne();

		return PageResponseDto.toDto(new PageImpl<>(dtos, pageable, total));
	}

	@Override
	public void updateBoard(long boardId, String title, String content, Integer clover) {
		QBoard board = QBoard.board;

		var update = queryFactory.update(board);

		if (title != null) {
			update.set(board.title, title);
		}
		if (content != null) {
			update.set(board.content, content);
		}
		if (clover != null) {
			update.set(board.clover, clover);
		}

		update.where(board.id.eq(boardId)).execute();
	}
}
