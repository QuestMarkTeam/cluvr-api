package com.example.cluvrapi.domain.analytics.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUserBoardStat is a Querydsl query type for UserBoardStat
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserBoardStat extends EntityPathBase<UserBoardStat> {

    private static final long serialVersionUID = 2034485003L;

    public static final QUserBoardStat userBoardStat = new QUserBoardStat("userBoardStat");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final EnumPath<com.example.cluvrapi.domain.clover.enums.Tier> tier = createEnum("tier", com.example.cluvrapi.domain.clover.enums.Tier.class);

    public final NumberPath<Integer> totalAnswer = createNumber("totalAnswer", Integer.class);

    public final NumberPath<Integer> totalClover = createNumber("totalClover", Integer.class);

    public final NumberPath<Integer> totalQuestion = createNumber("totalQuestion", Integer.class);

    public final NumberPath<Integer> totalSelected = createNumber("totalSelected", Integer.class);

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QUserBoardStat(String variable) {
        super(UserBoardStat.class, forVariable(variable));
    }

    public QUserBoardStat(Path<? extends UserBoardStat> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserBoardStat(PathMetadata metadata) {
        super(UserBoardStat.class, metadata);
    }

}

