package com.zqksk.api.datasource.user.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QApiKeyEntity is a Querydsl query type for ApiKeyEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QApiKeyEntity extends EntityPathBase<ApiKeyEntity> {

    private static final long serialVersionUID = -1520555178L;

    public static final QApiKeyEntity apiKeyEntity = new QApiKeyEntity("apiKeyEntity");

    public final com.zqksk.api.datasource.QBaseEntity _super = new com.zqksk.api.datasource.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final StringPath key = createString("key");

    public final StringPath keyName = createString("keyName");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QApiKeyEntity(String variable) {
        super(ApiKeyEntity.class, forVariable(variable));
    }

    public QApiKeyEntity(Path<? extends ApiKeyEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QApiKeyEntity(PathMetadata metadata) {
        super(ApiKeyEntity.class, metadata);
    }

}

