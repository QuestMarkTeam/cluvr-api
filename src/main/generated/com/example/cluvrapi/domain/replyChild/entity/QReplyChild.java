package com.example.cluvrapi.domain.replyChild.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReplyChild is a Querydsl query type for ReplyChild
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReplyChild extends EntityPathBase<ReplyChild> {

    private static final long serialVersionUID = -893502912L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReplyChild replyChild = new QReplyChild("replyChild");

    public final com.example.cluvrapi.domain.common.entity.QBaseTimeEntity _super = new com.example.cluvrapi.domain.common.entity.QBaseTimeEntity(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    public final QMentionInfo mention;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final com.example.cluvrapi.domain.reply.entity.QReply parent;

    public final com.example.cluvrapi.domain.user.entity.QUser user;

    public QReplyChild(String variable) {
        this(ReplyChild.class, forVariable(variable), INITS);
    }

    public QReplyChild(Path<? extends ReplyChild> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReplyChild(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReplyChild(PathMetadata metadata, PathInits inits) {
        this(ReplyChild.class, metadata, inits);
    }

    public QReplyChild(Class<? extends ReplyChild> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.mention = inits.isInitialized("mention") ? new QMentionInfo(forProperty("mention")) : null;
        this.parent = inits.isInitialized("parent") ? new com.example.cluvrapi.domain.reply.entity.QReply(forProperty("parent"), inits.get("parent")) : null;
        this.user = inits.isInitialized("user") ? new com.example.cluvrapi.domain.user.entity.QUser(forProperty("user")) : null;
    }

}

