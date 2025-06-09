package com.example.cluvrapi.domain.reply.repository;

import java.util.List;

import jakarta.persistence.EntityManager;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.example.cluvrapi.domain.common.dto.PageResponseDto;
import com.example.cluvrapi.domain.reply.dto.response.QReadReplyResponseDto;
import com.example.cluvrapi.domain.reply.dto.response.ReadReplyResponseDto;
import com.example.cluvrapi.domain.reply.entity.QReply;
import com.example.cluvrapi.domain.user.entity.QUser;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class ReplyRepositoryImpl implements ReplyRepositoryCustom {
	JPAQueryFactory queryFactory;

	public ReplyRepositoryImpl(EntityManager entityManager) {
		this.queryFactory = new JPAQueryFactory(entityManager);
	}

	@Override
	public PageResponseDto<ReadReplyResponseDto> findAllRepliesByParent(long boardId, Long parentId,
		Pageable pageable) {
		QReply reply = QReply.reply;
		QUser user = QUser.user;
		BooleanBuilder builder = new BooleanBuilder();

		builder.and(reply.board.id.eq(boardId));
		if (parentId == null) {
			builder.and(reply.parent.id.isNull());
		} else {
			builder.and(reply.parent.id.eq(parentId));
		}
		builder.and(reply.isDeleted.eq(false));

		List<ReadReplyResponseDto> dtos = queryFactory
			.select(new QReadReplyResponseDto(reply.id, reply.content, reply.user.name, reply.createdAt))
			.from(reply)
			.join(reply.user, user)
			.where(builder)
			.orderBy(reply.createdAt.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long total = queryFactory
			.select(reply.count())
			.from(reply)
			.where(builder)
			.fetchOne();

		return PageResponseDto.toDto(new PageImpl<>(dtos, pageable, total));
	}
}
