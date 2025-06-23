package com.example.cluvrapi.domain.applicationForm.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProblemForm is a Querydsl query type for ProblemForm
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProblemForm extends EntityPathBase<ProblemForm> {

    private static final long serialVersionUID = -361756015L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProblemForm problemForm = new QProblemForm("problemForm");

    public final com.example.cluvrapi.domain.common.entity.QBaseTimeEntity _super = new com.example.cluvrapi.domain.common.entity.QBaseTimeEntity(this);

    public final com.example.cluvrapi.domain.club.entity.QClub club;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath gradingCriteria = createString("gradingCriteria");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isActive = createBoolean("isActive");

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final StringPath problemTemplate = createString("problemTemplate");

    public final StringPath submissionInstructions = createString("submissionInstructions");

    public QProblemForm(String variable) {
        this(ProblemForm.class, forVariable(variable), INITS);
    }

    public QProblemForm(Path<? extends ProblemForm> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProblemForm(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProblemForm(PathMetadata metadata, PathInits inits) {
        this(ProblemForm.class, metadata, inits);
    }

    public QProblemForm(Class<? extends ProblemForm> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.club = inits.isInitialized("club") ? new com.example.cluvrapi.domain.club.entity.QClub(forProperty("club")) : null;
    }

}

