package com.example.cluvrapi.domain.applicationForm.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QApplicationForm is a Querydsl query type for ApplicationForm
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QApplicationForm extends EntityPathBase<ApplicationForm> {

    private static final long serialVersionUID = -924936766L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QApplicationForm applicationForm = new QApplicationForm("applicationForm");

    public final com.example.cluvrapi.domain.club.entity.QClub club;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final EnumPath<com.example.cluvrapi.domain.joinRequest.enums.JoinType> joinType = createEnum("joinType", com.example.cluvrapi.domain.joinRequest.enums.JoinType.class);

    public final StringPath problemForm = createString("problemForm");

    public final StringPath submissionForm = createString("submissionForm");

    public final com.example.cluvrapi.domain.user.entity.QUser user;

    public QApplicationForm(String variable) {
        this(ApplicationForm.class, forVariable(variable), INITS);
    }

    public QApplicationForm(Path<? extends ApplicationForm> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QApplicationForm(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QApplicationForm(PathMetadata metadata, PathInits inits) {
        this(ApplicationForm.class, metadata, inits);
    }

    public QApplicationForm(Class<? extends ApplicationForm> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.club = inits.isInitialized("club") ? new com.example.cluvrapi.domain.club.entity.QClub(forProperty("club"), inits.get("club")) : null;
        this.user = inits.isInitialized("user") ? new com.example.cluvrapi.domain.user.entity.QUser(forProperty("user")) : null;
    }

}

