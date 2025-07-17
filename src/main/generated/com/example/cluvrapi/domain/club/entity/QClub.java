package com.example.cluvrapi.domain.club.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QClub is a Querydsl query type for Club
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QClub extends EntityPathBase<Club> {

    private static final long serialVersionUID = 915892936L;

    public static final QClub club = new QClub("club");

    public final com.example.cluvrapi.domain.common.entity.QBaseTimeEntity _super = new com.example.cluvrapi.domain.common.entity.QBaseTimeEntity(this);

    public final EnumPath<com.example.cluvrapi.domain.club.enums.ClubType> clubType = createEnum("clubType", com.example.cluvrapi.domain.club.enums.ClubType.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath description = createString("description");

    public final StringPath greeting = createString("greeting");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    public final BooleanPath isPublic = createBoolean("isPublic");

    public final EnumPath<com.example.cluvrapi.domain.club.enums.JoinType> joinType = createEnum("joinType", com.example.cluvrapi.domain.club.enums.JoinType.class);

    public final NumberPath<Integer> maxMemberCount = createNumber("maxMemberCount", Integer.class);

    public final NumberPath<Integer> minCloverRequirement = createNumber("minCloverRequirement", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final StringPath name = createString("name");

    public final StringPath posterUrl = createString("posterUrl");

    public QClub(String variable) {
        super(Club.class, forVariable(variable));
    }

    public QClub(Path<? extends Club> path) {
        super(path.getType(), path.getMetadata());
    }

    public QClub(PathMetadata metadata) {
        super(Club.class, metadata);
    }

}

