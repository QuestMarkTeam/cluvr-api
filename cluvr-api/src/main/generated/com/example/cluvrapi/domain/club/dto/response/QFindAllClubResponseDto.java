package com.example.cluvrapi.domain.club.dto.response;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.example.cluvrapi.domain.club.dto.response.QFindAllClubResponseDto is a Querydsl Projection type for FindAllClubResponseDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QFindAllClubResponseDto extends ConstructorExpression<FindAllClubResponseDto> {

    private static final long serialVersionUID = -41326811L;

    public QFindAllClubResponseDto(com.querydsl.core.types.Expression<Long> clubId, com.querydsl.core.types.Expression<Long> authorId, com.querydsl.core.types.Expression<String> name, com.querydsl.core.types.Expression<com.example.cluvrapi.domain.club.enums.ClubType> clubType, com.querydsl.core.types.Expression<com.example.cluvrapi.domain.category.enums.CategoryType> categoryDetail, com.querydsl.core.types.Expression<String> greeting, com.querydsl.core.types.Expression<String> postUrl, com.querydsl.core.types.Expression<Integer> maxMemberCounter) {
        super(FindAllClubResponseDto.class, new Class<?>[]{long.class, long.class, String.class, com.example.cluvrapi.domain.club.enums.ClubType.class, com.example.cluvrapi.domain.category.enums.CategoryType.class, String.class, String.class, int.class}, clubId, authorId, name, clubType, categoryDetail, greeting, postUrl, maxMemberCounter);
    }

}

