package com.zqksk.api.datasource.customer;

import com.zqksk.api.datasource.BaseEntity;
import com.zqksk.api.domain.CustomerDetail;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Table(name = "customers_detail")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomerDetailEntity extends BaseEntity {

    @Column(unique = true)
    @Comment("사업자 거래처 ID")
    private Long customerId;

    @Column(length = 50)
    @Comment("거래처 이메일")
    private String email;

    @Column(length = 20)
    @Comment("거래처 전화번호")
    private String telephoneNumber;

    @Column(length = 20)
    @Comment("거래처 휴대전화번호")
    private String cellphoneNumber;

    @Column(length = 100)
    @Comment("대표자명")
    private String representativeName;

    @Column(length = 20)
    @Comment("대표자 면허번호")
    private String representativeLicenseNumber;

    @Column(length = 20)
    @Comment("우편번호")
    private Long zipNumber;

    @Column(length = 200)
    @Comment("지번 주소")
    private String jibunAddress;

    @Column(length = 200)
    @Comment("지번 상세주소")
    private String jibunAddressDetail;

    @Column(length = 200)
    @Comment("도로명 주소")
    private String streetAddress;

    @Column(length = 200)
    @Comment("도로명 상세주소")
    private String streetAddressDetail;

    @Builder
    public CustomerDetailEntity(Long customerId, String email, String telephoneNumber, String cellphoneNumber, String representativeName, String representativeLicenseNumber, Long zipNumber, String jibunAddress, String jibunAddressDetail, String streetAddress, String streetAddressDetail) {
        this.customerId = customerId;
        this.email = email;
        this.telephoneNumber = telephoneNumber;
        this.cellphoneNumber = cellphoneNumber;
        this.representativeName = representativeName;
        this.representativeLicenseNumber = representativeLicenseNumber;
        this.zipNumber = zipNumber;
        this.jibunAddress = jibunAddress;
        this.jibunAddressDetail = jibunAddressDetail;
        this.streetAddress = streetAddress;
        this.streetAddressDetail = streetAddressDetail;
    }

    public CustomerDetail toCustomerDetail() {
        return new CustomerDetail(
                customerId,
                cellphoneNumber,
                telephoneNumber,
                email,
                zipNumber,
                jibunAddress,
                jibunAddressDetail,
                streetAddress,
                streetAddressDetail,
                representativeLicenseNumber,
                representativeName
        );
    }
}
