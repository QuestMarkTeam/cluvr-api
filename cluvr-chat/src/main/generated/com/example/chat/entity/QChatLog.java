package com.example.chat.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QChatLog is a Querydsl query type for ChatLog
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QChatLog extends EntityPathBase<ChatLog> {

    private static final long serialVersionUID = 1599361414L;

    public static final QChatLog chatLog = new QChatLog("chatLog");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath message = createString("message");

    public final NumberPath<Long> roomId = createNumber("roomId", Long.class);

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QChatLog(String variable) {
        super(ChatLog.class, forVariable(variable));
    }

    public QChatLog(Path<? extends ChatLog> path) {
        super(path.getType(), path.getMetadata());
    }

    public QChatLog(PathMetadata metadata) {
        super(ChatLog.class, metadata);
    }

}

