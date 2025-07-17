package com.example.cluvrapi.domain.join.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QJoinRequestAnswer is a Querydsl query type for JoinRequestAnswer
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QJoinRequestAnswer extends EntityPathBase<JoinRequestAnswer> {

    private static final long serialVersionUID = -1186867363L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QJoinRequestAnswer joinRequestAnswer = new QJoinRequestAnswer("joinRequestAnswer");

    public final StringPath answer = createString("answer");

    public final EnumPath<com.example.cluvrapi.domain.join.enums.FormFieldType> fieldType = createEnum("fieldType", com.example.cluvrapi.domain.join.enums.FormFieldType.class);

    public final NumberPath<Long> formFieldId = createNumber("formFieldId", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    public final QJoinRequest joinRequest;

    public QJoinRequestAnswer(String variable) {
        this(JoinRequestAnswer.class, forVariable(variable), INITS);
    }

    public QJoinRequestAnswer(Path<? extends JoinRequestAnswer> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QJoinRequestAnswer(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QJoinRequestAnswer(PathMetadata metadata, PathInits inits) {
        this(JoinRequestAnswer.class, metadata, inits);
    }

    public QJoinRequestAnswer(Class<? extends JoinRequestAnswer> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.joinRequest = inits.isInitialized("joinRequest") ? new QJoinRequest(forProperty("joinRequest"), inits.get("joinRequest")) : null;
    }

}

