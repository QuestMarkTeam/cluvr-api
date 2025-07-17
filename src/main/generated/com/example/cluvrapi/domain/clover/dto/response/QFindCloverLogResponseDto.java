package com.example.cluvrapi.domain.clover.dto.response;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.example.cluvrapi.domain.clover.dto.response.QFindCloverLogResponseDto is a Querydsl Projection type for FindCloverLogResponseDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QFindCloverLogResponseDto extends ConstructorExpression<FindCloverLogResponseDto> {

    private static final long serialVersionUID = -1126029988L;

    public QFindCloverLogResponseDto(com.querydsl.core.types.Expression<Long> userId, com.querydsl.core.types.Expression<String> description, com.querydsl.core.types.Expression<Integer> amount, com.querydsl.core.types.Expression<java.time.LocalDateTime> createdAt, com.querydsl.core.types.Expression<java.time.LocalDateTime> deletedAt) {
        super(FindCloverLogResponseDto.class, new Class<?>[]{long.class, String.class, int.class, java.time.LocalDateTime.class, java.time.LocalDateTime.class}, userId, description, amount, createdAt, deletedAt);
    }

}

