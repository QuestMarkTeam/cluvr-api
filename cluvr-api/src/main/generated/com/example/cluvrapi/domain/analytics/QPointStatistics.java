package com.example.cluvrapi.domain.analytics;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;

import javax.annotation.processing.Generated;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;

/**
 * QPointStatistics is a Querydsl query type for PointStat
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPointStatistics extends EntityPathBase<PointStatistics> {

	private static final long serialVersionUID = 1488099116L;

	private static final PathInits INITS = PathInits.DIRECT2;

	public static final QPointStatistics pointStatistics = new QPointStatistics("pointStatistics");

	public final NumberPath<Long> id = createNumber("id", Long.class);

	public final NumberPath<Integer> point = createNumber("point", Integer.class);

	public final com.example.cluvrapi.domain.user.entity.QUser user;

	public QPointStatistics(String variable) {
		this(PointStatistics.class, forVariable(variable), INITS);
	}

	public QPointStatistics(Path<? extends PointStatistics> path) {
		this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
	}

	public QPointStatistics(PathMetadata metadata) {
		this(metadata, PathInits.getFor(metadata, INITS));
	}

	public QPointStatistics(PathMetadata metadata, PathInits inits) {
		this(PointStatistics.class, metadata, inits);
	}

	public QPointStatistics(Class<? extends PointStatistics> type, PathMetadata metadata, PathInits inits) {
		super(type, metadata, inits);
		this.user =
			inits.isInitialized("user") ? new com.example.cluvrapi.domain.user.entity.QUser(forProperty("user")) : null;
	}

}

