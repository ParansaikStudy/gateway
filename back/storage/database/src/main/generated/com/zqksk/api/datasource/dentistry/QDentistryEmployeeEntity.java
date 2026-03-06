package com.zqksk.api.datasource.dentistry;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QDentistryEmployeeEntity is a Querydsl query type for DentistryEmployeeEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDentistryEmployeeEntity extends EntityPathBase<DentistryEmployeeEntity> {

    private static final long serialVersionUID = -583250607L;

    public static final QDentistryEmployeeEntity dentistryEmployeeEntity = new QDentistryEmployeeEntity("dentistryEmployeeEntity");

    public final com.zqksk.api.datasource.QBaseEntity _super = new com.zqksk.api.datasource.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath employeeName = createString("employeeName");

    public final StringPath employeeNo = createString("employeeNo");

    public final StringPath hospitalId = createString("hospitalId");

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final StringPath medicalSpecialty = createString("medicalSpecialty");

    public final NumberPath<Integer> pgType = createNumber("pgType", Integer.class);

    public final ArrayPath<byte[], Byte> profile = createArray("profile", byte[].class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QDentistryEmployeeEntity(String variable) {
        super(DentistryEmployeeEntity.class, forVariable(variable));
    }

    public QDentistryEmployeeEntity(Path<? extends DentistryEmployeeEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDentistryEmployeeEntity(PathMetadata metadata) {
        super(DentistryEmployeeEntity.class, metadata);
    }

}

