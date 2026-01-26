package com.zqksk.api.datasource.patient;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPatientEntity is a Querydsl query type for PatientEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPatientEntity extends EntityPathBase<PatientEntity> {

    private static final long serialVersionUID = -1015222727L;

    public static final QPatientEntity patientEntity = new QPatientEntity("patientEntity");

    public final com.zqksk.api.datasource.QBaseEntity _super = new com.zqksk.api.datasource.QBaseEntity(this);

    public final StringPath address = createString("address");

    public final DatePath<java.time.LocalDate> birthDate = createDate("birthDate", java.time.LocalDate.class);

    public final EnumPath<BirthDataDivision> birthDateDivision = createEnum("birthDateDivision", BirthDataDivision.class);

    public final StringPath cellphoneNumber = createString("cellphoneNumber");

    public final NumberPath<Long> chartSerialNumber = createNumber("chartSerialNumber", Long.class);

    public final StringPath consentSignatureId = createString("consentSignatureId");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath deletedYn = createString("deletedYn");

    public final StringPath detailAddress = createString("detailAddress");

    public final StringPath doctorId = createString("doctorId");

    public final StringPath email = createString("email");

    public final StringPath familyId = createString("familyId");

    public final DatePath<java.time.LocalDate> firstVisitDate = createDate("firstVisitDate", java.time.LocalDate.class);

    public final StringPath hygienistId = createString("hygienistId");

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final NumberPath<Long> imageId = createNumber("imageId", Long.class);

    public final DatePath<java.time.LocalDate> lastVisitDate = createDate("lastVisitDate", java.time.LocalDate.class);

    public final StringPath name = createString("name");

    public final NumberPath<Long> patientId = createNumber("patientId", Long.class);

    public final NumberPath<Long> patientRelationId = createNumber("patientRelationId", Long.class);

    public final StringPath personalInformationAgreeYn = createString("personalInformationAgreeYn");

    public final StringPath protectorCellphoneNumber = createString("protectorCellphoneNumber");

    public final StringPath residentRegistrationNumber = createString("residentRegistrationNumber");

    public final StringPath rrn = createString("rrn");

    public final StringPath smsReceiveYn = createString("smsReceiveYn");

    public final StringPath telephoneNumber = createString("telephoneNumber");

    public final StringPath uid = createString("uid");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final StringPath visitPathId = createString("visitPathId");

    public final StringPath zipCode = createString("zipCode");

    public QPatientEntity(String variable) {
        super(PatientEntity.class, forVariable(variable));
    }

    public QPatientEntity(Path<? extends PatientEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPatientEntity(PathMetadata metadata) {
        super(PatientEntity.class, metadata);
    }

}

