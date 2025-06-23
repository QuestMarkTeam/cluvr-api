package com.example.cluvrapi.domain.clover.dto.response;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.example.cluvrapi.domain.clover.dto.response.QFindCloverResponseDto is a Querydsl Projection type for FindCloverResponseDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QFindCloverResponseDto extends ConstructorExpression<FindCloverResponseDto> {

    private static final long serialVersionUID = 334480316L;

    public QFindCloverResponseDto(com.querydsl.core.types.Expression<Integer> score, com.querydsl.core.types.Expression<com.example.cluvrapi.domain.clover.enums.Tier> tier, com.querydsl.core.types.Expression<Long> userId) {
        super(FindCloverResponseDto.class, new Class<?>[]{int.class, com.example.cluvrapi.domain.clover.enums.Tier.class, long.class}, score, tier, userId);
    }

}

