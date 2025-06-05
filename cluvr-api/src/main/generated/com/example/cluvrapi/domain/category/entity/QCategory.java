package com.example.cluvrapi.domain.category.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCategory is a Querydsl query type for Category
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCategory extends EntityPathBase<Category> {

    private static final long serialVersionUID = 1407474392L;

    public static final QCategory category = new QCategory("category");

    public final EnumPath<com.example.cluvrapi.domain.category.enums.CategoryType> categoryType = createEnum("categoryType", com.example.cluvrapi.domain.category.enums.CategoryType.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> targetId = createNumber("targetId", Long.class);

    public final EnumPath<com.example.cluvrapi.domain.category.enums.CategoryTargetType> targetType = createEnum("targetType", com.example.cluvrapi.domain.category.enums.CategoryTargetType.class);

    public QCategory(String variable) {
        super(Category.class, forVariable(variable));
    }

    public QCategory(Path<? extends Category> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCategory(PathMetadata metadata) {
        super(Category.class, metadata);
    }

}

