package com.zqksk.api.datasource.user.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QRoleScreenAccessEntity is a Querydsl query type for RoleScreenAccessEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRoleScreenAccessEntity extends EntityPathBase<RoleScreenAccessEntity> {

    private static final long serialVersionUID = -1446460489L;

    public static final QRoleScreenAccessEntity roleScreenAccessEntity = new QRoleScreenAccessEntity("roleScreenAccessEntity");

    public final com.zqksk.api.datasource.QBaseEntity _super = new com.zqksk.api.datasource.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final NumberPath<Long> roleId = createNumber("roleId", Long.class);

    public final NumberPath<Long> screenId = createNumber("screenId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QRoleScreenAccessEntity(String variable) {
        super(RoleScreenAccessEntity.class, forVariable(variable));
    }

    public QRoleScreenAccessEntity(Path<? extends RoleScreenAccessEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QRoleScreenAccessEntity(PathMetadata metadata) {
        super(RoleScreenAccessEntity.class, metadata);
    }

}

