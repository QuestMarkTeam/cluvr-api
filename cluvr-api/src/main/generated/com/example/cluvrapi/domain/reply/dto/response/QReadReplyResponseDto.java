package com.example.cluvrapi.domain.reply.dto.response;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.example.cluvrapi.domain.reply.dto.response.QReadReplyResponseDto is a Querydsl Projection type for ReadReplyResponseDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QReadReplyResponseDto extends ConstructorExpression<ReadReplyResponseDto> {

    private static final long serialVersionUID = 804674885L;

    public QReadReplyResponseDto(com.querydsl.core.types.Expression<Long> id, com.querydsl.core.types.Expression<String> content, com.querydsl.core.types.Expression<String> userName, com.querydsl.core.types.Expression<java.time.LocalDateTime> createdAt) {
        super(ReadReplyResponseDto.class, new Class<?>[]{long.class, String.class, String.class, java.time.LocalDateTime.class}, id, content, userName, createdAt);
    }

}

