package com.zqksk.api.datasource.user.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUserAuthEntity is a Querydsl query type for UserAuthEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserAuthEntity extends EntityPathBase<UserAuthEntity> {

    private static final long serialVersionUID = -1592991868L;

    public static final QUserAuthEntity userAuthEntity = new QUserAuthEntity("userAuthEntity");

    public final com.zqksk.api.datasource.QBaseEntity _super = new com.zqksk.api.datasource.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final NumberPath<Long> roleId = createNumber("roleId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QUserAuthEntity(String variable) {
        super(UserAuthEntity.class, forVariable(variable));
    }

    public QUserAuthEntity(Path<? extends UserAuthEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserAuthEntity(PathMetadata metadata) {
        super(UserAuthEntity.class, metadata);
    }

}

