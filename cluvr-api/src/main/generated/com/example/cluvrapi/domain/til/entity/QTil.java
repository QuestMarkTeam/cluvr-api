package com.example.cluvrapi.domain.til.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTil is a Querydsl query type for Til
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTil extends EntityPathBase<Til> {

    private static final long serialVersionUID = 580998498L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTil til = new QTil("til");

    public final com.example.cluvrapi.domain.common.entity.QBaseTimeEntity _super = new com.example.cluvrapi.domain.common.entity.QBaseTimeEntity(this);

    public final com.example.cluvrapi.domain.club.entity.QClub club;

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final StringPath title = createString("title");

    public final com.example.cluvrapi.domain.user.entity.QUser user;

    public QTil(String variable) {
        this(Til.class, forVariable(variable), INITS);
    }

    public QTil(Path<? extends Til> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTil(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTil(PathMetadata metadata, PathInits inits) {
        this(Til.class, metadata, inits);
    }

    public QTil(Class<? extends Til> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.club = inits.isInitialized("club") ? new com.example.cluvrapi.domain.club.entity.QClub(forProperty("club"), inits.get("club")) : null;
        this.user = inits.isInitialized("user") ? new com.example.cluvrapi.domain.user.entity.QUser(forProperty("user")) : null;
    }

}

