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
	public List<ReadBoardsResponseDto> findAllBoardsByCategory(CategoryType category, int pageNumber, int pageSize) {
		QBoard board = QBoard.board;
		QUser user = QUser.user;

		return queryFactory
			.select(new QReadBoardsResponseDto(
				board.id,
				board.title,
				board.content,
				board.view,
				user.name,
				board.createdAt,
				board.modifiedAt
			))
			.where(board.isSelected.isFalse().and(board.category.eq(category)))
			.offset(pageNumber - 1)
			.limit(pageSize)
			.fetch();
	}

	@Override
	public PageResponseDto<ReadMyBoardsResponseDto> findBoardsByUser(long userId, Pageable pageable) {
		QBoard board = QBoard.board;

		List<ReadMyBoardsResponseDto> dtos = queryFactory
			.select(new QReadMyBoardsResponseDto(board.id, board.title, board.content, board.createdAt))
			.from(board)
			.where(board.user.id.eq(userId))
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
