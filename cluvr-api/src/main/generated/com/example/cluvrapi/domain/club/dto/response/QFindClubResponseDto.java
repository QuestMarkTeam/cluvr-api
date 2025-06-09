package com.example.cluvrapi.domain.club.dto.response;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.example.cluvrapi.domain.club.dto.response.QFindClubResponseDto is a Querydsl Projection type for FindClubResponseDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QFindClubResponseDto extends ConstructorExpression<FindClubResponseDto> {

    private static final long serialVersionUID = -1964752822L;

    public QFindClubResponseDto(com.querydsl.core.types.Expression<com.example.cluvrapi.domain.club.enums.ClubType> clubType, com.querydsl.core.types.Expression<String> name, com.querydsl.core.types.Expression<String> categoryDetail, com.querydsl.core.types.Expression<String> greeting, com.querydsl.core.types.Expression<String> description, com.querydsl.core.types.Expression<String> postUrl, com.querydsl.core.types.Expression<java.time.LocalDateTime> createAt) {
        super(FindClubResponseDto.class, new Class<?>[]{com.example.cluvrapi.domain.club.enums.ClubType.class, String.class, String.class, String.class, String.class, String.class, java.time.LocalDateTime.class}, clubType, name, categoryDetail, greeting, description, postUrl, createAt);
    }

}

