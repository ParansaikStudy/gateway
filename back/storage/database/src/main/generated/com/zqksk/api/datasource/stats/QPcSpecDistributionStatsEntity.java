package com.zqksk.api.datasource.stats;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPcSpecDistributionStatsEntity is a Querydsl query type for PcSpecDistributionStatsEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPcSpecDistributionStatsEntity extends EntityPathBase<PcSpecDistributionStatsEntity> {

    private static final long serialVersionUID = 764704667L;

    public static final QPcSpecDistributionStatsEntity pcSpecDistributionStatsEntity = new QPcSpecDistributionStatsEntity("pcSpecDistributionStatsEntity");

    public final com.zqksk.api.datasource.QBaseEntity _super = new com.zqksk.api.datasource.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final NumberPath<Integer> pcCount = createNumber("pcCount", Integer.class);

    public final NumberPath<Integer> programType = createNumber("programType", Integer.class);

    public final NumberPath<java.math.BigDecimal> rate = createNumber("rate", java.math.BigDecimal.class);

    public final NumberPath<Integer> specType = createNumber("specType", Integer.class);

    public final DatePath<java.time.LocalDate> statDate = createDate("statDate", java.time.LocalDate.class);

    public final NumberPath<Integer> totalPcCount = createNumber("totalPcCount", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final NumberPath<Integer> workType = createNumber("workType", Integer.class);

    public QPcSpecDistributionStatsEntity(String variable) {
        super(PcSpecDistributionStatsEntity.class, forVariable(variable));
    }

    public QPcSpecDistributionStatsEntity(Path<? extends PcSpecDistributionStatsEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPcSpecDistributionStatsEntity(PathMetadata metadata) {
        super(PcSpecDistributionStatsEntity.class, metadata);
    }

}

