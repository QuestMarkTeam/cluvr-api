package com.example.cluvrapi.domain.reply.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReplyReactions is a Querydsl query type for ReplyReactions
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReplyReactions extends EntityPathBase<ReplyReactions> {

    private static final long serialVersionUID = 1158052808L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReplyReactions replyReactions = new QReplyReactions("replyReactions");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QReply reply;

    public final StringPath type = createString("type");

    public final com.example.cluvrapi.domain.user.entity.QUser user;

    public QReplyReactions(String variable) {
        this(ReplyReactions.class, forVariable(variable), INITS);
    }

    public QReplyReactions(Path<? extends ReplyReactions> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReplyReactions(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReplyReactions(PathMetadata metadata, PathInits inits) {
        this(ReplyReactions.class, metadata, inits);
    }

    public QReplyReactions(Class<? extends ReplyReactions> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.reply = inits.isInitialized("reply") ? new QReply(forProperty("reply"), inits.get("reply")) : null;
        this.user = inits.isInitialized("user") ? new com.example.cluvrapi.domain.user.entity.QUser(forProperty("user")) : null;
    }

}

