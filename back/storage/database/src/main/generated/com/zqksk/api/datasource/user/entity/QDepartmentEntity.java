package com.zqksk.api.datasource.user.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QDepartmentEntity is a Querydsl query type for DepartmentEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDepartmentEntity extends EntityPathBase<DepartmentEntity> {

    private static final long serialVersionUID = -1806932989L;

    public static final QDepartmentEntity departmentEntity = new QDepartmentEntity("departmentEntity");

    public final com.zqksk.api.datasource.QBaseEntity _super = new com.zqksk.api.datasource.QBaseEntity(this);

    public final StringPath code = createString("code");

    public final StringPath costCenterCode = createString("costCenterCode");

    public final StringPath costCenterName = createString("costCenterName");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Integer> departmentLevelOrder = createNumber("departmentLevelOrder", Integer.class);

    public final DatePath<java.time.LocalDate> effectiveEndDate = createDate("effectiveEndDate", java.time.LocalDate.class);

    public final StringPath effectiveEndYn = createString("effectiveEndYn");

    public final DatePath<java.time.LocalDate> effectiveStartDate = createDate("effectiveStartDate", java.time.LocalDate.class);

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final StringPath name = createString("name");

    public final NumberPath<Integer> sortOrder = createNumber("sortOrder", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final StringPath upperCode = createString("upperCode");

    public QDepartmentEntity(String variable) {
        super(DepartmentEntity.class, forVariable(variable));
    }

    public QDepartmentEntity(Path<? extends DepartmentEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDepartmentEntity(PathMetadata metadata) {
        super(DepartmentEntity.class, metadata);
    }

}

