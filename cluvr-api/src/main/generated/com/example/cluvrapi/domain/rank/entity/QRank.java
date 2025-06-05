package com.example.cluvrapi.domain.rank.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRank is a Querydsl query type for Rank
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRank extends EntityPathBase<Rank> {

    private static final long serialVersionUID = -951633740L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRank rank = new QRank("rank");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> score = createNumber("score", Integer.class);

    public final EnumPath<com.example.cluvrapi.domain.rank.eunms.Tier> tier = createEnum("tier", com.example.cluvrapi.domain.rank.eunms.Tier.class);

    public final com.example.cluvrapi.domain.user.entity.QUser user;

    public QRank(String variable) {
        this(Rank.class, forVariable(variable), INITS);
    }

    public QRank(Path<? extends Rank> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRank(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRank(PathMetadata metadata, PathInits inits) {
        this(Rank.class, metadata, inits);
    }

    public QRank(Class<? extends Rank> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.example.cluvrapi.domain.user.entity.QUser(forProperty("user")) : null;
    }

}

