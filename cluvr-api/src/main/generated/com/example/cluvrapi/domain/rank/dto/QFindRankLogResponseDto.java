package com.example.cluvrapi.domain.clover.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;

import javax.annotation.processing.Generated;

/**
 * com.example.cluvrapi.domain.rank.dto.QFindRankLogResponseDto is a Querydsl Projection type for FindCloverLogResponseDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QFindRankLogResponseDto extends ConstructorExpression<FindRankLogResponseDto> {

	private static final long serialVersionUID = -662298863L;

	public QFindRankLogResponseDto(com.querydsl.core.types.Expression<Long> userId,
		com.querydsl.core.types.Expression<String> description, com.querydsl.core.types.Expression<Integer> amount,
		com.querydsl.core.types.Expression<java.time.LocalDateTime> createdAt,
		com.querydsl.core.types.Expression<java.time.LocalDateTime> deletedAt) {
		super(FindRankLogResponseDto.class,
			new Class<?>[] {long.class, String.class, int.class, java.time.LocalDateTime.class,
				java.time.LocalDateTime.class}, userId, description, amount, createdAt, deletedAt);
	}

}

