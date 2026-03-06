package com.zqksk.api.datasource.user.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QTestUserEntity is a Querydsl query type for TestUserEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTestUserEntity extends EntityPathBase<TestUserEntity> {

    private static final long serialVersionUID = 830853998L;

    public static final QTestUserEntity testUserEntity = new QTestUserEntity("testUserEntity");

    public final com.zqksk.api.datasource.QBaseEntity _super = new com.zqksk.api.datasource.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath employeeNo = createString("employeeNo");

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final StringPath name = createString("name");

    public final StringPath password = createString("password");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QTestUserEntity(String variable) {
        super(TestUserEntity.class, forVariable(variable));
    }

    public QTestUserEntity(Path<? extends TestUserEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTestUserEntity(PathMetadata metadata) {
        super(TestUserEntity.class, metadata);
    }

}

