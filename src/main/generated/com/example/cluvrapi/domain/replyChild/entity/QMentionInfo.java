package com.example.cluvrapi.domain.replyChild.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMentionInfo is a Querydsl query type for MentionInfo
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QMentionInfo extends BeanPath<MentionInfo> {

    private static final long serialVersionUID = -1207326326L;

    public static final QMentionInfo mentionInfo = new QMentionInfo("mentionInfo");

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public final StringPath userName = createString("userName");

    public QMentionInfo(String variable) {
        super(MentionInfo.class, forVariable(variable));
    }

    public QMentionInfo(Path<? extends MentionInfo> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMentionInfo(PathMetadata metadata) {
        super(MentionInfo.class, metadata);
    }

}

