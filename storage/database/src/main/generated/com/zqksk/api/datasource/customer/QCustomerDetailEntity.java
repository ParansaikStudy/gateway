package com.zqksk.api.datasource.customer;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCustomerDetailEntity is a Querydsl query type for CustomerDetailEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCustomerDetailEntity extends EntityPathBase<CustomerDetailEntity> {

    private static final long serialVersionUID = -800461060L;

    public static final QCustomerDetailEntity customerDetailEntity = new QCustomerDetailEntity("customerDetailEntity");

    public final com.zqksk.api.datasource.QBaseEntity _super = new com.zqksk.api.datasource.QBaseEntity(this);

    public final StringPath cellphoneNumber = createString("cellphoneNumber");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> customerId = createNumber("customerId", Long.class);

    public final StringPath email = createString("email");

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final StringPath jibunAddress = createString("jibunAddress");

    public final StringPath jibunAddressDetail = createString("jibunAddressDetail");

    public final StringPath representativeLicenseNumber = createString("representativeLicenseNumber");

    public final StringPath representativeName = createString("representativeName");

    public final StringPath streetAddress = createString("streetAddress");

    public final StringPath streetAddressDetail = createString("streetAddressDetail");

    public final StringPath telephoneNumber = createString("telephoneNumber");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final NumberPath<Long> zipNumber = createNumber("zipNumber", Long.class);

    public QCustomerDetailEntity(String variable) {
        super(CustomerDetailEntity.class, forVariable(variable));
    }

    public QCustomerDetailEntity(Path<? extends CustomerDetailEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCustomerDetailEntity(PathMetadata metadata) {
        super(CustomerDetailEntity.class, metadata);
    }

}

