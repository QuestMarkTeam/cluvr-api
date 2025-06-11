package com.example.cluvrapi.domain.clover.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCloverLog is a Querydsl query type for CloverLog
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCloverLog extends EntityPathBase<CloverLog> {

    private static final long serialVersionUID = -2036764402L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCloverLog cloverLog = new QCloverLog("cloverLog");

    public final NumberPath<Integer> amount = createNumber("amount", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> deletedAt = createDateTime("deletedAt", java.time.LocalDateTime.class);

    public final StringPath description = createString("description");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.example.cluvrapi.domain.user.entity.QUser user;

    public QCloverLog(String variable) {
        this(CloverLog.class, forVariable(variable), INITS);
    }

    public QCloverLog(Path<? extends CloverLog> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCloverLog(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCloverLog(PathMetadata metadata, PathInits inits) {
        this(CloverLog.class, metadata, inits);
    }

    public QCloverLog(Class<? extends CloverLog> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.example.cluvrapi.domain.user.entity.QUser(forProperty("user")) : null;
    }

}

