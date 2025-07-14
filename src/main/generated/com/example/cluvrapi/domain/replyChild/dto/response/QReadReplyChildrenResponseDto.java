package com.example.cluvrapi.domain.replyChild.dto.response;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.example.cluvrapi.domain.replyChild.dto.response.QReadReplyChildrenResponseDto is a Querydsl Projection type for ReadReplyChildrenResponseDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QReadReplyChildrenResponseDto extends ConstructorExpression<ReadReplyChildrenResponseDto> {

    private static final long serialVersionUID = -2119381364L;

    public QReadReplyChildrenResponseDto(com.querydsl.core.types.Expression<Long> id, com.querydsl.core.types.Expression<String> userName, com.querydsl.core.types.Expression<String> content, com.querydsl.core.types.Expression<? extends com.example.cluvrapi.domain.replyChild.entity.MentionInfo> mentionInfo, com.querydsl.core.types.Expression<java.time.LocalDateTime> createdAt, com.querydsl.core.types.Expression<java.time.LocalDateTime> updatedAt) {
        super(ReadReplyChildrenResponseDto.class, new Class<?>[]{long.class, String.class, String.class, com.example.cluvrapi.domain.replyChild.entity.MentionInfo.class, java.time.LocalDateTime.class, java.time.LocalDateTime.class}, id, userName, content, mentionInfo, createdAt, updatedAt);
    }

}

