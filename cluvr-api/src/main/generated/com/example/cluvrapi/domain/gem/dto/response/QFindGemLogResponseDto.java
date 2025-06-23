package com.example.cluvrapi.domain.gem.dto.response;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.example.cluvrapi.domain.gem.dto.response.QFindGemLogResponseDto is a Querydsl Projection type for FindGemLogResponseDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QFindGemLogResponseDto extends ConstructorExpression<FindGemLogResponseDto> {

    private static final long serialVersionUID = -318964208L;

    public QFindGemLogResponseDto(com.querydsl.core.types.Expression<Long> userId, com.querydsl.core.types.Expression<String> description, com.querydsl.core.types.Expression<Integer> amount, com.querydsl.core.types.Expression<java.time.LocalDateTime> createdAt, com.querydsl.core.types.Expression<java.time.LocalDateTime> deletedAt) {
        super(FindGemLogResponseDto.class, new Class<?>[]{long.class, String.class, int.class, java.time.LocalDateTime.class, java.time.LocalDateTime.class}, userId, description, amount, createdAt, deletedAt);
    }

}

