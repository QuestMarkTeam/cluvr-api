package com.example.cluvrapi.domain.replyChild.repository;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.EntityManager;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.example.cluvrapi.domain.common.dto.PageResponseDto;
import com.example.cluvrapi.domain.reply.dto.response.QReadMyReplyResponseDto;
import com.example.cluvrapi.domain.reply.dto.response.ReadMyReplyResponseDto;
import com.example.cluvrapi.domain.reply.entity.QReply;
import com.example.cluvrapi.domain.replyChild.dto.response.QReadReplyChildrenResponseDto;
import com.example.cluvrapi.domain.replyChild.dto.response.ReadReplyChildrenResponseDto;
import com.example.cluvrapi.domain.replyChild.entity.MentionInfo;
import com.example.cluvrapi.domain.replyChild.entity.QReplyChild;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class ReplyChildRepositoryImpl implements ReplyChildRepositoryCustom {
	JPAQueryFactory queryFactory;

	public ReplyChildRepositoryImpl(EntityManager entityManager) {
		this.queryFactory = new JPAQueryFactory(entityManager);
	}

	@Override
	public PageResponseDto<ReadReplyChildrenResponseDto> findAllReplyChildrenByParent(long replyId, Pageable pageable) {
		QReplyChild replyChild = QReplyChild.replyChild;

		List<ReadReplyChildrenResponseDto> dtos = queryFactory
			.select(new QReadReplyChildrenResponseDto(replyChild.id, replyChild.user.name, replyChild.content, replyChild.mention,
				replyChild.createdAt, replyChild.modifiedAt))
			.from(replyChild)
			.where(replyChild.parent.id.eq(replyId).and(replyChild.isDeleted.isFalse()))
			.orderBy(replyChild.createdAt.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long total = queryFactory
			.select(replyChild.count())
			.from(replyChild)
			.where(replyChild.parent.id.eq(replyId).and(replyChild.isDeleted.isFalse()))
			.fetchOne();

		return PageResponseDto.toDto(new PageImpl<>(dtos, pageable, total));
	}
}
