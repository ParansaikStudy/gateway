package com.zqksk.api.datasource.hygienist;

import com.zqksk.api.datasource.BaseEntity;
import com.zqksk.api.domain.hygienist.Hygienist;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Table(name = "hygienists")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HygienistEntity extends BaseEntity {
    @Column
    @Comment("치위생사 ID")
    private Long hygienistId;

    @Column
    @Comment("치위생사 이름")
    private String name;

    @Column
    @Comment("치위생사 면허번호")
    private Long licenseNumber;

    @Column
    @Comment("병원 ID")
    private Long hospitalId;

    @Column
    @Comment("병원 이름")
    private String hospitalName;

    @Builder
    public HygienistEntity(Long hygienistId, String name, Long licenseNumber, Long hospitalId, String hospitalName) {
        this.hygienistId = hygienistId;
        this.name = name;
        this.licenseNumber = licenseNumber;
        this.hospitalId = hospitalId;
        this.hospitalName = hospitalName;
    }

    public Hygienist toHygienist() {
        return new Hygienist(super.getId(), hygienistId, name, licenseNumber, hospitalId, hospitalName);
    }
}
