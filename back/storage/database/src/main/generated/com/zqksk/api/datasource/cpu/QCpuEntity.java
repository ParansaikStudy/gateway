package com.zqksk.api.datasource.cpu;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCpuEntity is a Querydsl query type for CpuEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCpuEntity extends EntityPathBase<CpuEntity> {

    private static final long serialVersionUID = -1440517377L;

    public static final QCpuEntity cpuEntity = new QCpuEntity("cpuEntity");

    public final com.zqksk.api.datasource.QBaseEntity _super = new com.zqksk.api.datasource.QBaseEntity(this);

    public final StringPath brand = createString("brand");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final StringPath name = createString("name");

    public final NumberPath<Integer> score = createNumber("score", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QCpuEntity(String variable) {
        super(CpuEntity.class, forVariable(variable));
    }

    public QCpuEntity(Path<? extends CpuEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCpuEntity(PathMetadata metadata) {
        super(CpuEntity.class, metadata);
    }

}

