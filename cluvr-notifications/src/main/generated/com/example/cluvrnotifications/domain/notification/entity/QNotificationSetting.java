package com.example.cluvrnotifications.domain.notification.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QNotificationSetting is a Querydsl query type for NotificationSetting
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QNotificationSetting extends EntityPathBase<NotificationSetting> {

    private static final long serialVersionUID = 1564799728L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QNotificationSetting notificationSetting = new QNotificationSetting("notificationSetting");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isEnabled = createBoolean("isEnabled");

    public final EnumPath<com.example.cluvrnotifications.domain.notification.enums.NotificationType> notificationType = createEnum("notificationType", com.example.cluvrnotifications.domain.notification.enums.NotificationType.class);

    public final com.example.cluvrnotifications.domain.user.entity.QUser user;

    public QNotificationSetting(String variable) {
        this(NotificationSetting.class, forVariable(variable), INITS);
    }

    public QNotificationSetting(Path<? extends NotificationSetting> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QNotificationSetting(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QNotificationSetting(PathMetadata metadata, PathInits inits) {
        this(NotificationSetting.class, metadata, inits);
    }

    public QNotificationSetting(Class<? extends NotificationSetting> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.example.cluvrnotifications.domain.user.entity.QUser(forProperty("user")) : null;
    }

}

