package com.example.cluvrapi.domain.gem.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QGemLog is a Querydsl query type for GemLog
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QGemLog extends EntityPathBase<GemLog> {

    private static final long serialVersionUID = -1726151294L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QGemLog gemLog = new QGemLog("gemLog");

    public final EnumPath<com.example.cluvrapi.domain.gem.enums.GemActionType> actionType = createEnum("actionType", com.example.cluvrapi.domain.gem.enums.GemActionType.class);

    public final NumberPath<Integer> amount = createNumber("amount", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> deletedAt = createDateTime("deletedAt", java.time.LocalDateTime.class);

    public final StringPath description = createString("description");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.example.cluvrapi.domain.user.entity.QUser user;

    public QGemLog(String variable) {
        this(GemLog.class, forVariable(variable), INITS);
    }

    public QGemLog(Path<? extends GemLog> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QGemLog(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QGemLog(PathMetadata metadata, PathInits inits) {
        this(GemLog.class, metadata, inits);
    }

    public QGemLog(Class<? extends GemLog> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.example.cluvrapi.domain.user.entity.QUser(forProperty("user")) : null;
    }

}

