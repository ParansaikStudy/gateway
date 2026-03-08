package com.zqksk.api.datasource.log;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QLogEntity is a Querydsl query type for LogEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QLogEntity extends EntityPathBase<LogEntity> {

    private static final long serialVersionUID = -897925577L;

    public static final QLogEntity logEntity = new QLogEntity("logEntity");

    public final com.zqksk.api.datasource.QBaseEntity _super = new com.zqksk.api.datasource.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath errorCode = createString("errorCode");

    public final StringPath etc = createString("etc");

    public final NumberPath<Integer> eventType = createNumber("eventType", Integer.class);

    public final StringPath hospitalId = createString("hospitalId");

    public final StringPath hospitalName = createString("hospitalName");

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final StringPath ip = createString("ip");

    public final StringPath log = createString("log");

    public final StringPath logDetail = createString("logDetail");

    public final StringPath macAddress = createString("macAddress");

    public final StringPath patientUid = createString("patientUid");

    public final StringPath pcName = createString("pcName");

    public final NumberPath<Integer> programType = createNumber("programType", Integer.class);

    public final StringPath rootMenu = createString("rootMenu");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final StringPath userId = createString("userId");

    public final StringPath version = createString("version");

    public final StringPath viewId = createString("viewId");

    public final StringPath viewName = createString("viewName");

    public final DateTimePath<java.time.LocalDateTime> workDatetime = createDateTime("workDatetime", java.time.LocalDateTime.class);

    public final NumberPath<Integer> workType = createNumber("workType", Integer.class);

    public QLogEntity(String variable) {
        super(LogEntity.class, forVariable(variable));
    }

    public QLogEntity(Path<? extends LogEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QLogEntity(PathMetadata metadata) {
        super(LogEntity.class, metadata);
    }

}

