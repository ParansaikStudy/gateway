package com.zqksk.api.datasource.hygienist;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QHygienistEntity is a Querydsl query type for HygienistEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QHygienistEntity extends EntityPathBase<HygienistEntity> {

    private static final long serialVersionUID = 2056302475L;

    public static final QHygienistEntity hygienistEntity = new QHygienistEntity("hygienistEntity");

    public final com.zqksk.api.datasource.QBaseEntity _super = new com.zqksk.api.datasource.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> hospitalId = createNumber("hospitalId", Long.class);

    public final StringPath hospitalName = createString("hospitalName");

    public final NumberPath<Long> hygienistId = createNumber("hygienistId", Long.class);

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final NumberPath<Long> licenseNumber = createNumber("licenseNumber", Long.class);

    public final StringPath name = createString("name");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QHygienistEntity(String variable) {
        super(HygienistEntity.class, forVariable(variable));
    }

    public QHygienistEntity(Path<? extends HygienistEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QHygienistEntity(PathMetadata metadata) {
        super(HygienistEntity.class, metadata);
    }

}

