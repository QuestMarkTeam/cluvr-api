package com.example.cluvrapi.domain.notice.dto.response;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.example.cluvrapi.domain.notice.dto.response.QInfoNoticeResponseDto is a Querydsl Projection type for InfoNoticeResponseDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QInfoNoticeResponseDto extends ConstructorExpression<InfoNoticeResponseDto> {

    private static final long serialVersionUID = -430240655L;

    public QInfoNoticeResponseDto(com.querydsl.core.types.Expression<Long> noticeId, com.querydsl.core.types.Expression<Long> authorId, com.querydsl.core.types.Expression<String> title, com.querydsl.core.types.Expression<String> contents) {
        super(InfoNoticeResponseDto.class, new Class<?>[]{long.class, long.class, String.class, String.class}, noticeId, authorId, title, contents);
    }

}

