package com.zqksk.api.datasource.log;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAccessLogEntity is a Querydsl query type for AccessLogEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAccessLogEntity extends EntityPathBase<AccessLogEntity> {

    private static final long serialVersionUID = 1922745651L;

    public static final QAccessLogEntity accessLogEntity = new QAccessLogEntity("accessLogEntity");

    public final com.zqksk.api.datasource.QBaseEntity _super = new com.zqksk.api.datasource.QBaseEntity(this);

    public final NumberPath<Integer> action = createNumber("action", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath description = createString("description");

    public final StringPath elementName = createString("elementName");

    public final NumberPath<Integer> eventType = createNumber("eventType", Integer.class);

    public final StringPath functionName = createString("functionName");

    public final StringPath hospitalId = createString("hospitalId");

    public final StringPath hospitalName = createString("hospitalName");

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final StringPath ip = createString("ip");

    public final StringPath loadingSpeed = createString("loadingSpeed");

    public final StringPath macAddress = createString("macAddress");

    public final StringPath pcName = createString("pcName");

    public final NumberPath<Integer> programType = createNumber("programType", Integer.class);

    public final StringPath remark = createString("remark");

    public final StringPath rootMenu = createString("rootMenu");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final StringPath userId = createString("userId");

    public final StringPath version = createString("version");

    public final StringPath viewId = createString("viewId");

    public final StringPath viewName = createString("viewName");

    public final DateTimePath<java.time.LocalDateTime> workDatetime = createDateTime("workDatetime", java.time.LocalDateTime.class);

    public QAccessLogEntity(String variable) {
        super(AccessLogEntity.class, forVariable(variable));
    }

    public QAccessLogEntity(Path<? extends AccessLogEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAccessLogEntity(PathMetadata metadata) {
        super(AccessLogEntity.class, metadata);
    }

}

