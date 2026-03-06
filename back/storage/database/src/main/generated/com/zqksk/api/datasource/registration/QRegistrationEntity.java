package com.zqksk.api.datasource.registration;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QRegistrationEntity is a Querydsl query type for RegistrationEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRegistrationEntity extends EntityPathBase<RegistrationEntity> {

    private static final long serialVersionUID = -1690043605L;

    public static final QRegistrationEntity registrationEntity = new QRegistrationEntity("registrationEntity");

    public final com.zqksk.api.datasource.QBaseEntity _super = new com.zqksk.api.datasource.QBaseEntity(this);

    public final NumberPath<Long> chairId = createNumber("chairId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> dentalId = createNumber("dentalId", Long.class);

    public final StringPath doctorId = createString("doctorId");

    public final StringPath hygienistId = createString("hygienistId");

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final EnumPath<PatientDivision> patientDivision = createEnum("patientDivision", PatientDivision.class);

    public final NumberPath<Long> patientStatus = createNumber("patientStatus", Long.class);

    public final DateTimePath<java.time.LocalDateTime> registrationDate = createDateTime("registrationDate", java.time.LocalDateTime.class);

    public final StringPath registrationId = createString("registrationId");

    public final NumberPath<Long> registrationSequence = createNumber("registrationSequence", Long.class);

    public final StringPath registrationStatus = createString("registrationStatus");

    public final DateTimePath<java.time.LocalDateTime> reservationDate = createDateTime("reservationDate", java.time.LocalDateTime.class);

    public final StringPath uid = createString("uid");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QRegistrationEntity(String variable) {
        super(RegistrationEntity.class, forVariable(variable));
    }

    public QRegistrationEntity(Path<? extends RegistrationEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QRegistrationEntity(PathMetadata metadata) {
        super(RegistrationEntity.class, metadata);
    }

}

