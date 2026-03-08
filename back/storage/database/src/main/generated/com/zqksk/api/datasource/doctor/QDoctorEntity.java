package com.zqksk.api.datasource.doctor;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QDoctorEntity is a Querydsl query type for DoctorEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDoctorEntity extends EntityPathBase<DoctorEntity> {

    private static final long serialVersionUID = 1263808299L;

    public static final QDoctorEntity doctorEntity = new QDoctorEntity("doctorEntity");

    public final com.zqksk.api.datasource.QBaseEntity _super = new com.zqksk.api.datasource.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> doctorId = createNumber("doctorId", Long.class);

    public final NumberPath<Long> hospitalId = createNumber("hospitalId", Long.class);

    public final StringPath hospitalName = createString("hospitalName");

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final NumberPath<Long> licenseNumber = createNumber("licenseNumber", Long.class);

    public final StringPath name = createString("name");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QDoctorEntity(String variable) {
        super(DoctorEntity.class, forVariable(variable));
    }

    public QDoctorEntity(Path<? extends DoctorEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDoctorEntity(PathMetadata metadata) {
        super(DoctorEntity.class, metadata);
    }

}

