package com.example.cluvrapi.domain.join.dto.response;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.example.cluvrapi.domain.join.dto.response.QMyJoinRequestResponseDto is a Querydsl Projection type for MyJoinRequestResponseDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QMyJoinRequestResponseDto extends ConstructorExpression<MyJoinRequestResponseDto> {

    private static final long serialVersionUID = -1835202114L;

    public QMyJoinRequestResponseDto(com.querydsl.core.types.Expression<Long> joinRequestId, com.querydsl.core.types.Expression<Long> clubId, com.querydsl.core.types.Expression<com.example.cluvrapi.domain.join.enums.JoinStatus> joinStatus, com.querydsl.core.types.Expression<com.example.cluvrapi.domain.club.enums.JoinType> joinType) {
        super(MyJoinRequestResponseDto.class, new Class<?>[]{long.class, long.class, com.example.cluvrapi.domain.join.enums.JoinStatus.class, com.example.cluvrapi.domain.club.enums.JoinType.class}, joinRequestId, clubId, joinStatus, joinType);
    }

}

