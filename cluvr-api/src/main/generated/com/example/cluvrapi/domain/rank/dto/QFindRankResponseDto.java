package com.example.cluvrapi.domain.clover.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;

import javax.annotation.processing.Generated;

/**
 * com.example.cluvrapi.domain.rank.dto.QFindRankResponseDto is a Querydsl Projection type for FindCloverResponseDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QFindRankResponseDto extends ConstructorExpression<FindRankResponseDto> {

	private static final long serialVersionUID = -1892930009L;

	public QFindRankResponseDto(com.querydsl.core.types.Expression<Integer> score,
		com.querydsl.core.types.Expression<com.example.cluvrapi.domain.clover.eunms.Tier> tier,
		com.querydsl.core.types.Expression<Long> userId) {
		super(FindRankResponseDto.class,
			new Class<?>[] {int.class, com.example.cluvrapi.domain.clover.eunms.Tier.class, long.class}, score, tier,
			userId);
	}

}

