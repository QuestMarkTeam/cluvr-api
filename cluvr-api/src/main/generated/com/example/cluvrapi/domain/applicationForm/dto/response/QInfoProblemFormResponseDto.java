package com.example.cluvrapi.domain.applicationForm.dto.response;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.example.cluvrapi.domain.applicationForm.dto.response.QInfoProblemFormResponseDto is a Querydsl Projection type for InfoProblemFormResponseDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QInfoProblemFormResponseDto extends ConstructorExpression<InfoProblemFormResponseDto> {

    private static final long serialVersionUID = 2105847534L;

    public QInfoProblemFormResponseDto(com.querydsl.core.types.Expression<String> problemTemplate, com.querydsl.core.types.Expression<String> submissionInstructions, com.querydsl.core.types.Expression<String> gradingCriteria) {
        super(InfoProblemFormResponseDto.class, new Class<?>[]{String.class, String.class, String.class}, problemTemplate, submissionInstructions, gradingCriteria);
    }

}

