package com.example.cluvrnotifications.domain.notification.dto.response;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.example.cluvrnotifications.domain.notification.dto.response.QReadNotificationResponseDto is a Querydsl Projection type for ReadNotificationResponseDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QReadNotificationResponseDto extends ConstructorExpression<ReadNotificationResponseDto> {

    private static final long serialVersionUID = -198003691L;

    public QReadNotificationResponseDto(com.querydsl.core.types.Expression<Long> id, com.querydsl.core.types.Expression<String> content, com.querydsl.core.types.Expression<Boolean> isRead) {
        super(ReadNotificationResponseDto.class, new Class<?>[]{long.class, String.class, boolean.class}, id, content, isRead);
    }

}

