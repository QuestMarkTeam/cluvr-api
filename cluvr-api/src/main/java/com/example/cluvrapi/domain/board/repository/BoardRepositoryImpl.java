package com.example.cluvrapi.domain.board.repository;

import java.util.List;

import jakarta.persistence.EntityManager;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.example.cluvrapi.domain.board.dto.response.QReadBoardsResponseDto;
import com.example.cluvrapi.domain.board.dto.response.QReadMyBoardsResponseDto;
import com.example.cluvrapi.domain.board.dto.response.ReadBoardsResponseDto;
import com.example.cluvrapi.domain.board.dto.response.ReadMyBoardsResponseDto;
import com.example.cluvrapi.domain.board.entity.Board;
import com.example.cluvrapi.domain.board.entity.QBoard;
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
	public PageResponseDto<ReadBoardsResponseDto> findAllBoardsByCategory(CategoryType category, Pageable pageable) {
		QBoard board = QBoard.board;
		QUser user = QUser.user;

		List<ReadBoardsResponseDto> dtos = queryFactory
			.select(new QReadBoardsResponseDto(
				board.id,
				board.title,
				board.content,
				board.viewCount,
				board.user.name,
				board.createdAt,
				board.modifiedAt
			))
			.from(board)
			// .join(board.user, user).fetchJoin() // -이거 요청 계속 실패하네요  ㅠㅠ N+1 문제 때문에 조인해야할거같은데
			.where(
				board.isDeleted.isFalse()
					.and(category != null ? board.category.eq(category) : null)
			)
			.orderBy(board.createdAt.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long total = queryFactory
			.select(board.count())
			.from(board)
			.where(board.isDeleted.isFalse().and(board.category.eq(category)))
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
}
