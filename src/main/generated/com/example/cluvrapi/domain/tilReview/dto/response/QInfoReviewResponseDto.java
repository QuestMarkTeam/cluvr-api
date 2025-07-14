package com.example.cluvrapi.domain.tilReview.dto.response;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.example.cluvrapi.domain.tilReview.dto.response.QInfoReviewResponseDto is a Querydsl Projection type for InfoReviewResponseDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QInfoReviewResponseDto extends ConstructorExpression<InfoReviewResponseDto> {

    private static final long serialVersionUID = 901801784L;

    public QInfoReviewResponseDto(com.querydsl.core.types.Expression<Long> reviewId, com.querydsl.core.types.Expression<Long> clubId, com.querydsl.core.types.Expression<Long> tilId, com.querydsl.core.types.Expression<String> tilContent, com.querydsl.core.types.Expression<Boolean> reviewed, com.querydsl.core.types.Expression<Integer> score, com.querydsl.core.types.Expression<String> summary, com.querydsl.core.types.Expression<String> feedback) {
        super(InfoReviewResponseDto.class, new Class<?>[]{long.class, long.class, long.class, String.class, boolean.class, int.class, String.class, String.class}, reviewId, clubId, tilId, tilContent, reviewed, score, summary, feedback);
    }

}

