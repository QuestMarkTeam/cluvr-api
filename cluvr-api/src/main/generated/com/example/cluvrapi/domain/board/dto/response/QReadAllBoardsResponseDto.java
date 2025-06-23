package com.example.cluvrapi.domain.board.dto.response;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.example.cluvrapi.domain.board.dto.response.QReadAllBoardsResponseDto is a Querydsl Projection type for ReadAllBoardsResponseDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QReadAllBoardsResponseDto extends ConstructorExpression<ReadAllBoardsResponseDto> {

    private static final long serialVersionUID = -2113736483L;

    public QReadAllBoardsResponseDto(com.querydsl.core.types.Expression<Long> id, com.querydsl.core.types.Expression<String> title, com.querydsl.core.types.Expression<String> content, com.querydsl.core.types.Expression<Long> viewCount, com.querydsl.core.types.Expression<String> userName, com.querydsl.core.types.Expression<java.time.LocalDateTime> createdAt, com.querydsl.core.types.Expression<java.time.LocalDateTime> updatedAt) {
        super(ReadAllBoardsResponseDto.class, new Class<?>[]{long.class, String.class, String.class, long.class, String.class, java.time.LocalDateTime.class, java.time.LocalDateTime.class}, id, title, content, viewCount, userName, createdAt, updatedAt);
    }

}

