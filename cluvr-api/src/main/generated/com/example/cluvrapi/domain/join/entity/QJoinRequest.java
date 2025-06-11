package com.example.cluvrapi.domain.join.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QJoinRequest is a Querydsl query type for JoinRequest
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QJoinRequest extends EntityPathBase<JoinRequest> {

    private static final long serialVersionUID = 1146943167L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QJoinRequest joinRequest = new QJoinRequest("joinRequest");

    public final com.example.cluvrapi.domain.club.entity.QClub club;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    public final EnumPath<com.example.cluvrapi.domain.join.enums.JoinStatus> joinStatus = createEnum("joinStatus", com.example.cluvrapi.domain.join.enums.JoinStatus.class);

    public final EnumPath<com.example.cluvrapi.domain.club.enums.JoinType> joinType = createEnum("joinType", com.example.cluvrapi.domain.club.enums.JoinType.class);

    public final com.example.cluvrapi.domain.user.entity.QUser user;

    public QJoinRequest(String variable) {
        this(JoinRequest.class, forVariable(variable), INITS);
    }

    public QJoinRequest(Path<? extends JoinRequest> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QJoinRequest(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QJoinRequest(PathMetadata metadata, PathInits inits) {
        this(JoinRequest.class, metadata, inits);
    }

    public QJoinRequest(Class<? extends JoinRequest> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.club = inits.isInitialized("club") ? new com.example.cluvrapi.domain.club.entity.QClub(forProperty("club"), inits.get("club")) : null;
        this.user = inits.isInitialized("user") ? new com.example.cluvrapi.domain.user.entity.QUser(forProperty("user")) : null;
    }

}

