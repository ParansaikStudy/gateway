package com.zqksk.api.datasource.notices;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QNoticesEntity is a Querydsl query type for NoticesEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QNoticesEntity extends EntityPathBase<NoticesEntity> {

    private static final long serialVersionUID = -164881819L;

    public static final QNoticesEntity noticesEntity = new QNoticesEntity("noticesEntity");

    public final com.zqksk.api.datasource.QBaseEntity _super = new com.zqksk.api.datasource.QBaseEntity(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath deleteYn = createString("deleteYn");

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final NumberPath<Long> index = createNumber("index", Long.class);

    public final DatePath<java.time.LocalDate> noticeEndDate = createDate("noticeEndDate", java.time.LocalDate.class);

    public final DatePath<java.time.LocalDate> noticeStartDate = createDate("noticeStartDate", java.time.LocalDate.class);

    public final NumberPath<Integer> pgType = createNumber("pgType", Integer.class);

    public final StringPath title = createString("title");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QNoticesEntity(String variable) {
        super(NoticesEntity.class, forVariable(variable));
    }

    public QNoticesEntity(Path<? extends NoticesEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QNoticesEntity(PathMetadata metadata) {
        super(NoticesEntity.class, metadata);
    }

}

