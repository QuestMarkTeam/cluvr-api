package com.example.cluvrapi.domain.reply.dto.response;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.example.cluvrapi.domain.reply.dto.response.QReadMyReplyResponseDto is a Querydsl Projection type for ReadMyReplyResponseDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QReadMyReplyResponseDto extends ConstructorExpression<ReadMyReplyResponseDto> {

    private static final long serialVersionUID = 789475025L;

    public QReadMyReplyResponseDto(com.querydsl.core.types.Expression<Long> id, com.querydsl.core.types.Expression<String> content, com.querydsl.core.types.Expression<java.time.LocalDateTime> createdAt) {
        super(ReadMyReplyResponseDto.class, new Class<?>[]{long.class, String.class, java.time.LocalDateTime.class}, id, content, createdAt);
    }

}

