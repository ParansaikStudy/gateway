package com.zqksk.api.datasource.reservation;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QReservationEntity is a Querydsl query type for ReservationEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReservationEntity extends EntityPathBase<ReservationEntity> {

    private static final long serialVersionUID = -1184916153L;

    public static final QReservationEntity reservationEntity = new QReservationEntity("reservationEntity");

    public final com.zqksk.api.datasource.QBaseEntity _super = new com.zqksk.api.datasource.QBaseEntity(this);

    public final StringPath chartSerialNumber = createString("chartSerialNumber");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath doctorId = createString("doctorId");

    public final StringPath hygienistId = createString("hygienistId");

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final StringPath patientCellPhoneNumber = createString("patientCellPhoneNumber");

    public final EnumPath<com.zqksk.api.datasource.registration.PatientDivision> patientDivision = createEnum("patientDivision", com.zqksk.api.datasource.registration.PatientDivision.class);

    public final StringPath patientName = createString("patientName");

    public final StringPath reservationContents = createString("reservationContents");

    public final DateTimePath<java.time.LocalDateTime> reservationDate = createDateTime("reservationDate", java.time.LocalDateTime.class);

    public final EnumPath<ReservationDivision> reservationDivision = createEnum("reservationDivision", ReservationDivision.class);

    public final StringPath reservationId = createString("reservationId");

    public final EnumPath<ReservationStatus> reservationStatus = createEnum("reservationStatus", ReservationStatus.class);

    public final StringPath uid = createString("uid");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QReservationEntity(String variable) {
        super(ReservationEntity.class, forVariable(variable));
    }

    public QReservationEntity(Path<? extends ReservationEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QReservationEntity(PathMetadata metadata) {
        super(ReservationEntity.class, metadata);
    }

}

