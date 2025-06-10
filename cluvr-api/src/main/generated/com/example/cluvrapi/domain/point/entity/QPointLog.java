package com.example.cluvrapi.domain.gem.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;

import javax.annotation.processing.Generated;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;

/**
 * QPointLog is a Querydsl query type for GemLog
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPointLog extends EntityPathBase<PointLog> {

	private static final long serialVersionUID = 2065235042L;

	private static final PathInits INITS = PathInits.DIRECT2;

	public static final QPointLog pointLog = new QPointLog("pointLog");

	public final NumberPath<Integer> amount = createNumber("amount", Integer.class);

	public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt",
		java.time.LocalDateTime.class);

	public final DateTimePath<java.time.LocalDateTime> deletedAt = createDateTime("deletedAt",
		java.time.LocalDateTime.class);

	public final StringPath description = createString("description");

	public final NumberPath<Long> id = createNumber("id", Long.class);

	public final com.example.cluvrapi.domain.user.entity.QUser user;

	public QPointLog(String variable) {
		this(PointLog.class, forVariable(variable), INITS);
	}

	public QPointLog(Path<? extends PointLog> path) {
		this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
	}

	public QPointLog(PathMetadata metadata) {
		this(metadata, PathInits.getFor(metadata, INITS));
	}

	public QPointLog(PathMetadata metadata, PathInits inits) {
		this(PointLog.class, metadata, inits);
	}

	public QPointLog(Class<? extends PointLog> type, PathMetadata metadata, PathInits inits) {
		super(type, metadata, inits);
		this.user =
			inits.isInitialized("user") ? new com.example.cluvrapi.domain.user.entity.QUser(forProperty("user")) : null;
	}

}

