package com.example.cluvrapi.domain.reaction.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReaction is a Querydsl query type for Reaction
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReaction extends EntityPathBase<Reaction> {

    private static final long serialVersionUID = 555933486L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReaction reaction = new QReaction("reaction");

    public final com.example.cluvrapi.domain.board.entity.QBoard board;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final EnumPath<com.example.cluvrapi.domain.reaction.enums.ReactionType> reactionType = createEnum("reactionType", com.example.cluvrapi.domain.reaction.enums.ReactionType.class);

    public final com.example.cluvrapi.domain.reply.entity.QReply reply;

    public final com.example.cluvrapi.domain.user.entity.QUser user;

    public QReaction(String variable) {
        this(Reaction.class, forVariable(variable), INITS);
    }

    public QReaction(Path<? extends Reaction> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReaction(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReaction(PathMetadata metadata, PathInits inits) {
        this(Reaction.class, metadata, inits);
    }

    public QReaction(Class<? extends Reaction> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.board = inits.isInitialized("board") ? new com.example.cluvrapi.domain.board.entity.QBoard(forProperty("board"), inits.get("board")) : null;
        this.reply = inits.isInitialized("reply") ? new com.example.cluvrapi.domain.reply.entity.QReply(forProperty("reply"), inits.get("reply")) : null;
        this.user = inits.isInitialized("user") ? new com.example.cluvrapi.domain.user.entity.QUser(forProperty("user")) : null;
    }

}

