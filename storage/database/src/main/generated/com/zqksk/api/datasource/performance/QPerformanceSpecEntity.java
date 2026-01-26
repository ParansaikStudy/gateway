package com.zqksk.api.datasource.performance;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPerformanceSpecEntity is a Querydsl query type for PerformanceSpecEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPerformanceSpecEntity extends EntityPathBase<PerformanceSpecEntity> {

    private static final long serialVersionUID = -911179862L;

    public static final QPerformanceSpecEntity performanceSpecEntity = new QPerformanceSpecEntity("performanceSpecEntity");

    public final com.zqksk.api.datasource.QBaseEntity _super = new com.zqksk.api.datasource.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final NumberPath<Integer> maxScore = createNumber("maxScore", Integer.class);

    public final NumberPath<Integer> minScore = createNumber("minScore", Integer.class);

    public final NumberPath<Integer> recScore = createNumber("recScore", Integer.class);

    public final NumberPath<Integer> softwareId = createNumber("softwareId", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final NumberPath<Integer> workType = createNumber("workType", Integer.class);

    public QPerformanceSpecEntity(String variable) {
        super(PerformanceSpecEntity.class, forVariable(variable));
    }

    public QPerformanceSpecEntity(Path<? extends PerformanceSpecEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPerformanceSpecEntity(PathMetadata metadata) {
        super(PerformanceSpecEntity.class, metadata);
    }

}

