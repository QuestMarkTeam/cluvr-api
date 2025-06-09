package com.example.cluvrapi.domain.board.dto.response;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.example.cluvrapi.domain.board.dto.response.QReadMyBoardsResponseDto is a Querydsl Projection type for ReadMyBoardsResponseDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QReadMyBoardsResponseDto extends ConstructorExpression<ReadMyBoardsResponseDto> {

    private static final long serialVersionUID = 2090945592L;

    public QReadMyBoardsResponseDto(com.querydsl.core.types.Expression<Long> id, com.querydsl.core.types.Expression<String> title, com.querydsl.core.types.Expression<String> content, com.querydsl.core.types.Expression<java.time.LocalDateTime> createdAt) {
        super(ReadMyBoardsResponseDto.class, new Class<?>[]{long.class, String.class, String.class, java.time.LocalDateTime.class}, id, title, content, createdAt);
    }

}

