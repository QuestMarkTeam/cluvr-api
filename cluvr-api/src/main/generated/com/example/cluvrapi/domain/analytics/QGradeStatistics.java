package com.example.cluvrapi.domain.analytics;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;

import javax.annotation.processing.Generated;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;

/**
 * QGradeStatistics is a Querydsl query type for UserBoardStat
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QGradeStatistics extends EntityPathBase<GradeStatistics> {

	private static final long serialVersionUID = 1529222323L;

	private static final PathInits INITS = PathInits.DIRECT2;

	public static final QGradeStatistics gradeStatistics = new QGradeStatistics("gradeStatistics");

	public final com.example.cluvrapi.domain.category.entity.QCategory category;

	public final NumberPath<Long> id = createNumber("id", Long.class);

	public final NumberPath<Integer> score = createNumber("score", Integer.class);

	public final com.example.cluvrapi.domain.user.entity.QUser user;

	public QGradeStatistics(String variable) {
		this(GradeStatistics.class, forVariable(variable), INITS);
	}

	public QGradeStatistics(Path<? extends GradeStatistics> path) {
		this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
	}

	public QGradeStatistics(PathMetadata metadata) {
		this(metadata, PathInits.getFor(metadata, INITS));
	}

	public QGradeStatistics(PathMetadata metadata, PathInits inits) {
		this(GradeStatistics.class, metadata, inits);
	}

	public QGradeStatistics(Class<? extends GradeStatistics> type, PathMetadata metadata, PathInits inits) {
		super(type, metadata, inits);
		this.category = inits.isInitialized("category") ?
			new com.example.cluvrapi.domain.category.entity.QCategory(forProperty("category")) : null;
		this.user =
			inits.isInitialized("user") ? new com.example.cluvrapi.domain.user.entity.QUser(forProperty("user")) : null;
	}

}

