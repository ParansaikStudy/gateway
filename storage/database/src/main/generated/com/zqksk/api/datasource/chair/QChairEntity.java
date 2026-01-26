package com.zqksk.api.datasource.chair;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QChairEntity is a Querydsl query type for ChairEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QChairEntity extends EntityPathBase<ChairEntity> {

    private static final long serialVersionUID = -561494919L;

    public static final QChairEntity chairEntity = new QChairEntity("chairEntity");

    public final com.zqksk.api.datasource.QBaseEntity _super = new com.zqksk.api.datasource.QBaseEntity(this);

    public final NumberPath<Long> chairId = createNumber("chairId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final StringPath manufacturer = createString("manufacturer");

    public final StringPath modelName = createString("modelName");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QChairEntity(String variable) {
        super(ChairEntity.class, forVariable(variable));
    }

    public QChairEntity(Path<? extends ChairEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QChairEntity(PathMetadata metadata) {
        super(ChairEntity.class, metadata);
    }

}

