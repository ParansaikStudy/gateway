package com.zqksk.api.datasource.customer;

import com.zqksk.api.datasource.BaseEntity;
import com.zqksk.api.domain.Customer;
import com.zqksk.api.domain.CustomerLocation;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Table(name = "customers")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomerEntity extends BaseEntity {

    @Column(unique = true, length = 20)
    @Comment("CIMS 사업자거래처 코드")
    private String cimsCustomerCode;

    @Column(unique = true, length = 20)
    @Comment("사업자 등록 번호")
    private Long corporateRegistrationNumber;

    @Column
    @Comment("치과별 구분 값")
    private Long dentalId;

    @Column(nullable = false, length = 100)
    @Comment("거래처명")
    private String name;

    @Column(length = 1)
    @Comment("거래처 상태")
    private String statusCode;

    @Column(length = 20)
    @Comment("시/도 코드")
    private String sidoCode;

    @Column
    @Comment("위도")
    private Double latitude;

    @Column
    @Comment("경도")
    private Double longitude;

    @Column(length = 50)
    @Comment("로고 파일 ID")
    private String logoFileId;

    @Builder
    public CustomerEntity(String cimsCustomerCode, Long corporateRegistrationNumber, Long dentalId, String name, String statusCode, String sidoCode, Double latitude, Double longitude, String logoFileId) {
        this.cimsCustomerCode = cimsCustomerCode;
        this.corporateRegistrationNumber = corporateRegistrationNumber;
        this.dentalId = dentalId;
        this.name = name;
        this.statusCode = statusCode;
        this.sidoCode = sidoCode;
        this.latitude = latitude;
        this.longitude = longitude;
        this.logoFileId = logoFileId;
    }

    public Customer toCustomer() {
        return new Customer(
            getId(),
                corporateRegistrationNumber,
                dentalId,
                name,
                sidoCode,
                statusCode,
                logoFileId,
                latitude,
                longitude
        );
    }

    public CustomerLocation toCustomerLocation() {
        return new CustomerLocation(
            cimsCustomerCode,
            latitude,
            longitude,
            logoFileId
        );
    }
}
