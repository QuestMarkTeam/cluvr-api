package com.example.cluvrapi.domain.clover.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QClover is a Querydsl query type for Clover
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QClover extends EntityPathBase<Clover> {

    private static final long serialVersionUID = 1825411670L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QClover clover = new QClover("clover");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> score = createNumber("score", Integer.class);

    public final EnumPath<com.example.cluvrapi.domain.clover.enums.Tier> tier = createEnum("tier", com.example.cluvrapi.domain.clover.enums.Tier.class);

    public final com.example.cluvrapi.domain.user.entity.QUser user;

    public QClover(String variable) {
        this(Clover.class, forVariable(variable), INITS);
    }

    public QClover(Path<? extends Clover> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QClover(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QClover(PathMetadata metadata, PathInits inits) {
        this(Clover.class, metadata, inits);
    }

    public QClover(Class<? extends Clover> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.example.cluvrapi.domain.user.entity.QUser(forProperty("user")) : null;
    }

}

