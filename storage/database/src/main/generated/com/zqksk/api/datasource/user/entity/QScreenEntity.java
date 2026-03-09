package com.zqksk.api.datasource.user.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QScreenEntity is a Querydsl query type for ScreenEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QScreenEntity extends EntityPathBase<ScreenEntity> {

    private static final long serialVersionUID = -1938275L;

    public static final QScreenEntity screenEntity = new QScreenEntity("screenEntity");

    public final com.zqksk.api.datasource.QBaseEntity _super = new com.zqksk.api.datasource.QBaseEntity(this);

    public final StringPath componentName = createString("componentName");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final StringPath name = createString("name");

    public final NumberPath<Long> parentId = createNumber("parentId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final StringPath url = createString("url");

    public QScreenEntity(String variable) {
        super(ScreenEntity.class, forVariable(variable));
    }

    public QScreenEntity(Path<? extends ScreenEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QScreenEntity(PathMetadata metadata) {
        super(ScreenEntity.class, metadata);
    }

}

