package com.zqksk.api.datasource.user.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUserScreenAccessEntity is a Querydsl query type for UserScreenAccessEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserScreenAccessEntity extends EntityPathBase<UserScreenAccessEntity> {

    private static final long serialVersionUID = -1946688564L;

    public static final QUserScreenAccessEntity userScreenAccessEntity = new QUserScreenAccessEntity("userScreenAccessEntity");

    public final com.zqksk.api.datasource.QBaseEntity _super = new com.zqksk.api.datasource.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final NumberPath<Long> screenId = createNumber("screenId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QUserScreenAccessEntity(String variable) {
        super(UserScreenAccessEntity.class, forVariable(variable));
    }

    public QUserScreenAccessEntity(Path<? extends UserScreenAccessEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserScreenAccessEntity(PathMetadata metadata) {
        super(UserScreenAccessEntity.class, metadata);
    }

}

