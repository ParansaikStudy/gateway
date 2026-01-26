package com.zqksk.api.datasource.user.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QEmployeeEntity is a Querydsl query type for EmployeeEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QEmployeeEntity extends EntityPathBase<EmployeeEntity> {

    private static final long serialVersionUID = 1153680863L;

    public static final QEmployeeEntity employeeEntity = new QEmployeeEntity("employeeEntity");

    public final com.zqksk.api.datasource.QBaseEntity _super = new com.zqksk.api.datasource.QBaseEntity(this);

    public final StringPath businessDutyCode = createString("businessDutyCode");

    public final StringPath concurrentPositionStatusYn = createString("concurrentPositionStatusYn");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final DatePath<java.time.LocalDate> departmentAssignEndDate = createDate("departmentAssignEndDate", java.time.LocalDate.class);

    public final DatePath<java.time.LocalDate> departmentAssignStartDate = createDate("departmentAssignStartDate", java.time.LocalDate.class);

    public final StringPath departmentCode = createString("departmentCode");

    public final StringPath dutyCode = createString("dutyCode");

    public final StringPath email = createString("email");

    public final DatePath<java.time.LocalDate> enterDate = createDate("enterDate", java.time.LocalDate.class);

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final StringPath jobGroupCode = createString("jobGroupCode");

    public final StringPath mobile = createString("mobile");

    public final StringPath name = createString("name");

    public final StringPath no = createString("no");

    public final StringPath positionCode = createString("positionCode");

    public final DatePath<java.time.LocalDate> resignDate = createDate("resignDate", java.time.LocalDate.class);

    public final StringPath statusCode = createString("statusCode");

    public final StringPath telephone = createString("telephone");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QEmployeeEntity(String variable) {
        super(EmployeeEntity.class, forVariable(variable));
    }

    public QEmployeeEntity(Path<? extends EmployeeEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QEmployeeEntity(PathMetadata metadata) {
        super(EmployeeEntity.class, metadata);
    }

}

