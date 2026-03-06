package com.zqksk.api.datasource.pc;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPcEntity is a Querydsl query type for PcEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPcEntity extends EntityPathBase<PcEntity> {

    private static final long serialVersionUID = 1789880363L;

    public static final QPcEntity pcEntity = new QPcEntity("pcEntity");

    public final com.zqksk.api.datasource.QBaseEntity _super = new com.zqksk.api.datasource.QBaseEntity(this);

    public final StringPath cpu = createString("cpu");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final DateTimePath<java.time.LocalDateTime> firstDatetime = createDateTime("firstDatetime", java.time.LocalDateTime.class);

    public final StringPath gpu = createString("gpu");

    public final StringPath hospitalId = createString("hospitalId");

    public final StringPath hospitalName = createString("hospitalName");

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final StringPath ip = createString("ip");

    public final DateTimePath<java.time.LocalDateTime> lastDatetime = createDateTime("lastDatetime", java.time.LocalDateTime.class);

    public final StringPath macAddress = createString("macAddress");

    public final StringPath memory = createString("memory");

    public final StringPath monResol = createString("monResol");

    public final StringPath os = createString("os");

    public final StringPath pcName = createString("pcName");

    public final NumberPath<Integer> perfSpecScore = createNumber("perfSpecScore", Integer.class);

    public final NumberPath<Integer> pgType = createNumber("pgType", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final StringPath version = createString("version");

    public final NumberPath<Integer> workType = createNumber("workType", Integer.class);

    public QPcEntity(String variable) {
        super(PcEntity.class, forVariable(variable));
    }

    public QPcEntity(Path<? extends PcEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPcEntity(PathMetadata metadata) {
        super(PcEntity.class, metadata);
    }

}

