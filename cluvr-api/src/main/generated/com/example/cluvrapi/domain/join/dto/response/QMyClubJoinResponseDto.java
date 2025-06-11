package com.example.cluvrapi.domain.join.dto.response;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.example.cluvrapi.domain.join.dto.response.QMyClubJoinResponseDto is a Querydsl Projection type for MyClubJoinResponseDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QMyClubJoinResponseDto extends ConstructorExpression<MyClubJoinResponseDto> {

    private static final long serialVersionUID = -1416384071L;

    public QMyClubJoinResponseDto(com.querydsl.core.types.Expression<Long> joinRequestId, com.querydsl.core.types.Expression<Long> userId, com.querydsl.core.types.Expression<com.example.cluvrapi.domain.join.enums.JoinStatus> joinStatus, com.querydsl.core.types.Expression<com.example.cluvrapi.domain.club.enums.JoinType> joinType) {
        super(MyClubJoinResponseDto.class, new Class<?>[]{long.class, long.class, com.example.cluvrapi.domain.join.enums.JoinStatus.class, com.example.cluvrapi.domain.club.enums.JoinType.class}, joinRequestId, userId, joinStatus, joinType);
    }

}

