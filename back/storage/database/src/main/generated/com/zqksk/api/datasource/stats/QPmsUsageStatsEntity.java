package com.zqksk.api.datasource.stats;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPmsUsageStatsEntity is a Querydsl query type for PmsUsageStatsEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPmsUsageStatsEntity extends EntityPathBase<PmsUsageStatsEntity> {

    private static final long serialVersionUID = 157186178L;

    public static final QPmsUsageStatsEntity pmsUsageStatsEntity = new QPmsUsageStatsEntity("pmsUsageStatsEntity");

    public final com.zqksk.api.datasource.QBaseEntity _super = new com.zqksk.api.datasource.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final NumberPath<Integer> programType = createNumber("programType", Integer.class);

    public final DatePath<java.time.LocalDate> statDate = createDate("statDate", java.time.LocalDate.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final NumberPath<Integer> usageCount = createNumber("usageCount", Integer.class);

    public QPmsUsageStatsEntity(String variable) {
        super(PmsUsageStatsEntity.class, forVariable(variable));
    }

    public QPmsUsageStatsEntity(Path<? extends PmsUsageStatsEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPmsUsageStatsEntity(PathMetadata metadata) {
        super(PmsUsageStatsEntity.class, metadata);
    }

}

