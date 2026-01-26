package com.zqksk.api.datasource.competitor;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCompetitorPcEntity is a Querydsl query type for CompetitorPcEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCompetitorPcEntity extends EntityPathBase<CompetitorPcEntity> {

    private static final long serialVersionUID = 205540702L;

    public static final QCompetitorPcEntity competitorPcEntity = new QCompetitorPcEntity("competitorPcEntity");

    public final com.zqksk.api.datasource.QBaseEntity _super = new com.zqksk.api.datasource.QBaseEntity(this);

    public final NumberPath<Integer> competitorId = createNumber("competitorId", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> competitorInstallDate = createDateTime("competitorInstallDate", java.time.LocalDateTime.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath hospitalId = createString("hospitalId");

    public final StringPath hospitalName = createString("hospitalName");

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final StringPath ip = createString("ip");

    public final StringPath macAddress = createString("macAddress");

    public final StringPath pcName = createString("pcName");

    public final NumberPath<Integer> pgType = createNumber("pgType", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final DateTimePath<java.time.LocalDateTime> workDatetime = createDateTime("workDatetime", java.time.LocalDateTime.class);

    public QCompetitorPcEntity(String variable) {
        super(CompetitorPcEntity.class, forVariable(variable));
    }

    public QCompetitorPcEntity(Path<? extends CompetitorPcEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCompetitorPcEntity(PathMetadata metadata) {
        super(CompetitorPcEntity.class, metadata);
    }

}

