package com.zqksk.api.datasource.stats;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QDentwebWeeklyInstallStats is a Querydsl query type for DentwebWeeklyInstallStats
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDentwebWeeklyInstallStats extends EntityPathBase<DentwebWeeklyInstallStats> {

    private static final long serialVersionUID = 88762045L;

    public static final QDentwebWeeklyInstallStats dentwebWeeklyInstallStats = new QDentwebWeeklyInstallStats("dentwebWeeklyInstallStats");

    public final com.zqksk.api.datasource.QBaseEntity _super = new com.zqksk.api.datasource.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final NumberPath<Integer> installCount = createNumber("installCount", Integer.class);

    public final NumberPath<Integer> programType = createNumber("programType", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final DatePath<java.time.LocalDate> weekEndDate = createDate("weekEndDate", java.time.LocalDate.class);

    public final DatePath<java.time.LocalDate> weekStartDate = createDate("weekStartDate", java.time.LocalDate.class);

    public QDentwebWeeklyInstallStats(String variable) {
        super(DentwebWeeklyInstallStats.class, forVariable(variable));
    }

    public QDentwebWeeklyInstallStats(Path<? extends DentwebWeeklyInstallStats> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDentwebWeeklyInstallStats(PathMetadata metadata) {
        super(DentwebWeeklyInstallStats.class, metadata);
    }

}

