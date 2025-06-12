package com.example.cluvrapi.domain.board.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBoardReaction is a Querydsl query type for BoardReaction
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBoardReaction extends EntityPathBase<BoardReaction> {

    private static final long serialVersionUID = 1528766347L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBoardReaction boardReaction = new QBoardReaction("boardReaction");

    public final QBoard board;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final EnumPath<com.example.cluvrapi.domain.board.enums.ReactionType> type = createEnum("type", com.example.cluvrapi.domain.board.enums.ReactionType.class);

    public final com.example.cluvrapi.domain.user.entity.QUser user;

    public QBoardReaction(String variable) {
        this(BoardReaction.class, forVariable(variable), INITS);
    }

    public QBoardReaction(Path<? extends BoardReaction> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBoardReaction(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBoardReaction(PathMetadata metadata, PathInits inits) {
        this(BoardReaction.class, metadata, inits);
    }

    public QBoardReaction(Class<? extends BoardReaction> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.board = inits.isInitialized("board") ? new QBoard(forProperty("board"), inits.get("board")) : null;
        this.user = inits.isInitialized("user") ? new com.example.cluvrapi.domain.user.entity.QUser(forProperty("user")) : null;
    }

}

