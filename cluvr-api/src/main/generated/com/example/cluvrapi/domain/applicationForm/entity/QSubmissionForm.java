package com.example.cluvrapi.domain.applicationForm.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSubmissionForm is a Querydsl query type for SubmissionForm
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSubmissionForm extends EntityPathBase<SubmissionForm> {

    private static final long serialVersionUID = 547935458L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSubmissionForm submissionForm = new QSubmissionForm("submissionForm");

    public final com.example.cluvrapi.domain.common.entity.QBaseTimeEntity _super = new com.example.cluvrapi.domain.common.entity.QBaseTimeEntity(this);

    public final com.example.cluvrapi.domain.club.entity.QClub club;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final StringPath submissionTemplate = createString("submissionTemplate");

    public QSubmissionForm(String variable) {
        this(SubmissionForm.class, forVariable(variable), INITS);
    }

    public QSubmissionForm(Path<? extends SubmissionForm> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSubmissionForm(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSubmissionForm(PathMetadata metadata, PathInits inits) {
        this(SubmissionForm.class, metadata, inits);
    }

    public QSubmissionForm(Class<? extends SubmissionForm> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.club = inits.isInitialized("club") ? new com.example.cluvrapi.domain.club.entity.QClub(forProperty("club"), inits.get("club")) : null;
    }

}

