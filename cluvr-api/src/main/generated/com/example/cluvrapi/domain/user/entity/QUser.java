package com.example.cluvrapi.domain.user.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = 1379205490L;

    public static final QUser user = new QUser("user");

    public final com.example.cluvrapi.domain.common.entity.QBaseTimeEntity _super = new com.example.cluvrapi.domain.common.entity.QBaseTimeEntity(this);

    public final DatePath<java.time.LocalDate> birthday = createDate("birthday", java.time.LocalDate.class);

    public final EnumPath<com.example.cluvrapi.domain.category.enums.CategoryType> categoryType = createEnum("categoryType", com.example.cluvrapi.domain.category.enums.CategoryType.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath email = createString("email");

    public final NumberPath<Integer> gem = createNumber("gem", Integer.class);

    public final EnumPath<com.example.cluvrapi.domain.user.entity.enums.Gender> gender = createEnum("gender", com.example.cluvrapi.domain.user.entity.enums.Gender.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imageUrl = createString("imageUrl");

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final StringPath name = createString("name");

    public final StringPath password = createString("password");

    public final StringPath phoneNumber = createString("phoneNumber");

    public final EnumPath<com.example.cluvrapi.domain.user.entity.enums.UserRole> userRole = createEnum("userRole", com.example.cluvrapi.domain.user.entity.enums.UserRole.class);

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

