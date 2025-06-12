package com.example.cluvrapi.domain.til.dto.response;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.example.cluvrapi.domain.til.dto.response.QInfoTilResponseDto is a Querydsl Projection type for InfoTilResponseDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QInfoTilResponseDto extends ConstructorExpression<InfoTilResponseDto> {

    private static final long serialVersionUID = -195352387L;

    public QInfoTilResponseDto(com.querydsl.core.types.Expression<Long> authorId, com.querydsl.core.types.Expression<Long> userId, com.querydsl.core.types.Expression<String> title, com.querydsl.core.types.Expression<String> content) {
        super(InfoTilResponseDto.class, new Class<?>[]{long.class, long.class, String.class, String.class}, authorId, userId, title, content);
    }

}

