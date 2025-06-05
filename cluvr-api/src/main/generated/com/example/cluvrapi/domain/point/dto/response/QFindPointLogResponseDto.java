package com.example.cluvrapi.domain.point.dto.response;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.example.cluvrapi.domain.point.dto.response.QFindPointLogResponseDto is a Querydsl Projection type for FindPointLogResponseDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QFindPointLogResponseDto extends ConstructorExpression<FindPointLogResponseDto> {

    private static final long serialVersionUID = -49439888L;

    public QFindPointLogResponseDto(com.querydsl.core.types.Expression<Long> userId, com.querydsl.core.types.Expression<String> description, com.querydsl.core.types.Expression<Integer> amount, com.querydsl.core.types.Expression<java.time.LocalDateTime> createdAt, com.querydsl.core.types.Expression<java.time.LocalDateTime> deletedAt) {
        super(FindPointLogResponseDto.class, new Class<?>[]{long.class, String.class, int.class, java.time.LocalDateTime.class, java.time.LocalDateTime.class}, userId, description, amount, createdAt, deletedAt);
    }

}

