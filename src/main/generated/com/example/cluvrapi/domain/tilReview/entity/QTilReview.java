package com.example.cluvrapi.domain.tilReview.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QTilReview is a Querydsl query type for TilReview
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTilReview extends EntityPathBase<TilReview> {

    private static final long serialVersionUID = -1013715742L;

    public static final QTilReview tilReview = new QTilReview("tilReview");

    public final com.example.cluvrapi.domain.common.entity.QBaseTimeEntity _super = new com.example.cluvrapi.domain.common.entity.QBaseTimeEntity(this);

    public final NumberPath<Long> clubId = createNumber("clubId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath feedback = createString("feedback");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final NumberPath<Long> requestId = createNumber("requestId", Long.class);

    public final BooleanPath reviewed = createBoolean("reviewed");

    public final NumberPath<Integer> score = createNumber("score", Integer.class);

    public final StringPath summary = createString("summary");

    public final StringPath tilContent = createString("tilContent");

    public final NumberPath<Long> tilId = createNumber("tilId", Long.class);

    public QTilReview(String variable) {
        super(TilReview.class, forVariable(variable));
    }

    public QTilReview(Path<? extends TilReview> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTilReview(PathMetadata metadata) {
        super(TilReview.class, metadata);
    }

}

