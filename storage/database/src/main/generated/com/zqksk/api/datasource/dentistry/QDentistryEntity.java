package com.zqksk.api.datasource.dentistry;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QDentistryEntity is a Querydsl query type for DentistryEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDentistryEntity extends EntityPathBase<DentistryEntity> {

    private static final long serialVersionUID = -865086205L;

    public static final QDentistryEntity dentistryEntity = new QDentistryEntity("dentistryEntity");

    public final com.zqksk.api.datasource.QBaseEntity _super = new com.zqksk.api.datasource.QBaseEntity(this);

    public final StringPath address = createString("address");

    public final StringPath businessRegistrationNumber = createString("businessRegistrationNumber");

    public final StringPath claimYn = createString("claimYn");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath dbUsableCapacity = createString("dbUsableCapacity");

    public final NumberPath<Integer> doctorCount = createNumber("doctorCount", Integer.class);

    public final StringPath electronicSignatureYn = createString("electronicSignatureYn");

    public final NumberPath<Integer> employeeCount = createNumber("employeeCount", Integer.class);

    public final StringPath hospitalId = createString("hospitalId");

    public final StringPath hospitalName = createString("hospitalName");

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final NumberPath<Integer> pgType = createNumber("pgType", Integer.class);

    public final NumberPath<Integer> pmsContractState = createNumber("pmsContractState", Integer.class);

    public final StringPath pmsDepositAmount = createString("pmsDepositAmount");

    public final DateTimePath<java.time.LocalDateTime> pmsJoinDate = createDateTime("pmsJoinDate", java.time.LocalDateTime.class);

    public final StringPath pmsRequestAmount = createString("pmsRequestAmount");

    public final NumberPath<Integer> pmsUnpaidCount = createNumber("pmsUnpaidCount", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> pmsWithdrawalDate = createDateTime("pmsWithdrawalDate", java.time.LocalDateTime.class);

    public final StringPath programMonthlyFee = createString("programMonthlyFee");

    public final StringPath sidoName = createString("sidoName");

    public final StringPath sigunguName = createString("sigunguName");

    public final StringPath telephone = createString("telephone");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final StringPath zipCode = createString("zipCode");

    public QDentistryEntity(String variable) {
        super(DentistryEntity.class, forVariable(variable));
    }

    public QDentistryEntity(Path<? extends DentistryEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDentistryEntity(PathMetadata metadata) {
        super(DentistryEntity.class, metadata);
    }

}

