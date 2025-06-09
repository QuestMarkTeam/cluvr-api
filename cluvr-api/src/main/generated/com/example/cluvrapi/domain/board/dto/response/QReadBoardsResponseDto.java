package com.example.cluvrapi.domain.board.dto.response;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.example.cluvrapi.domain.board.dto.response.QReadBoardsResponseDto is a Querydsl Projection type for ReadBoardsResponseDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QReadBoardsResponseDto extends ConstructorExpression<ReadBoardsResponseDto> {

    private static final long serialVersionUID = -1839350460L;

    public QReadBoardsResponseDto(com.querydsl.core.types.Expression<Long> id, com.querydsl.core.types.Expression<String> title, com.querydsl.core.types.Expression<String> content, com.querydsl.core.types.Expression<Integer> view, com.querydsl.core.types.Expression<String> userName, com.querydsl.core.types.Expression<java.time.LocalDateTime> createdAt, com.querydsl.core.types.Expression<java.time.LocalDateTime> updatedAt) {
        super(ReadBoardsResponseDto.class, new Class<?>[]{long.class, String.class, String.class, int.class, String.class, java.time.LocalDateTime.class, java.time.LocalDateTime.class}, id, title, content, view, userName, createdAt, updatedAt);
    }

}

