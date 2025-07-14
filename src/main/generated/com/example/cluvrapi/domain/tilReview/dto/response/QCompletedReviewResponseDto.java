package com.example.cluvrapi.domain.tilReview.dto.response;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.example.cluvrapi.domain.tilReview.dto.response.QCompletedReviewResponseDto is a Querydsl Projection type for CompletedReviewResponseDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QCompletedReviewResponseDto extends ConstructorExpression<CompletedReviewResponseDto> {

    private static final long serialVersionUID = -1858725509L;

    public QCompletedReviewResponseDto(com.querydsl.core.types.Expression<Long> reviewId, com.querydsl.core.types.Expression<Long> clubId, com.querydsl.core.types.Expression<Long> tilId, com.querydsl.core.types.Expression<String> tilContent, com.querydsl.core.types.Expression<Integer> score, com.querydsl.core.types.Expression<String> summary, com.querydsl.core.types.Expression<String> feedback) {
        super(CompletedReviewResponseDto.class, new Class<?>[]{long.class, long.class, long.class, String.class, int.class, String.class, String.class}, reviewId, clubId, tilId, tilContent, score, summary, feedback);
    }

}

