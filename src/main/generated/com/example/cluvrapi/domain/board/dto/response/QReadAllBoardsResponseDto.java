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

    public QReadAllBoardsResponseDto(com.querydsl.core.types.Expression<? extends com.example.cluvrapi.domain.board.entity.Board> board) {
        super(ReadAllBoardsResponseDto.class, new Class<?>[]{com.example.cluvrapi.domain.board.entity.Board.class}, board);
    }

}

