package com.example.cluvrapi.domain.join.dto.response;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.example.cluvrapi.domain.join.dto.response.QInfoJoinRequestResponseDto is a Querydsl Projection type for InfoJoinRequestResponseDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QInfoJoinRequestResponseDto extends ConstructorExpression<InfoJoinRequestResponseDto> {

    private static final long serialVersionUID = 439951616L;

    public QInfoJoinRequestResponseDto(com.querydsl.core.types.Expression<Long> joinRequestId, com.querydsl.core.types.Expression<Long> clubId, com.querydsl.core.types.Expression<Long> userId, com.querydsl.core.types.Expression<com.example.cluvrapi.domain.club.enums.JoinType> joinType, com.querydsl.core.types.Expression<String> answer) {
        super(InfoJoinRequestResponseDto.class, new Class<?>[]{long.class, long.class, long.class, com.example.cluvrapi.domain.club.enums.JoinType.class, String.class}, joinRequestId, clubId, userId, joinType, answer);
    }

}

