package com.zqksk.api.datasource.dentistry;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QDentistryClinicHoursEntity is a Querydsl query type for DentistryClinicHoursEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDentistryClinicHoursEntity extends EntityPathBase<DentistryClinicHoursEntity> {

    private static final long serialVersionUID = -1634804566L;

    public static final QDentistryClinicHoursEntity dentistryClinicHoursEntity = new QDentistryClinicHoursEntity("dentistryClinicHoursEntity");

    public final com.zqksk.api.datasource.QBaseEntity _super = new com.zqksk.api.datasource.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath dinnerTimeTerms = createString("dinnerTimeTerms");

    public final StringPath hospitalId = createString("hospitalId");

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final StringPath lunchTimeTerms = createString("lunchTimeTerms");

    public final StringPath treatTimeTerms = createString("treatTimeTerms");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QDentistryClinicHoursEntity(String variable) {
        super(DentistryClinicHoursEntity.class, forVariable(variable));
    }

    public QDentistryClinicHoursEntity(Path<? extends DentistryClinicHoursEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDentistryClinicHoursEntity(PathMetadata metadata) {
        super(DentistryClinicHoursEntity.class, metadata);
    }

}

