package com.example.cluvrapi.domain.reply.repository;

import java.util.List;

import jakarta.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import com.example.cluvrapi.domain.reply.dto.response.QReadReplyResponseDto;
import com.example.cluvrapi.domain.reply.dto.response.ReadReplyResponseDto;
import com.example.cluvrapi.domain.reply.entity.QReply;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class ReplyRepositoryImpl implements ReplyRepositoryCustom {
	JPAQueryFactory queryFactory;

	public ReplyRepositoryImpl(EntityManager entityManager) {
		this.queryFactory = new JPAQueryFactory(entityManager);
	}

	@Override
	public List<ReadReplyResponseDto> findAllRepliesByParent(long boardId, Long parentId, int pageNumber,
		int pageSize) {
		QReply reply = QReply.reply;
		BooleanBuilder builder = new BooleanBuilder();

		builder.and(reply.board.id.eq(boardId));
		builder.and(reply.parent.id.eq(parentId));
		builder.and(reply.isDeleted.eq(false));

		return queryFactory
			.select(new QReadReplyResponseDto(reply.id, reply.content, reply.user.name, reply.createdAt))
			.from(reply)
			.where(builder)
			.orderBy(reply.createdAt.desc())
			.offset((long)(pageNumber - 1) * pageSize)
			.limit(pageSize)
			.fetch();
	}
}
