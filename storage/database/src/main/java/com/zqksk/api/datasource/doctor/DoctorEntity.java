package com.zqksk.api.datasource.doctor;

import com.zqksk.api.datasource.BaseEntity;
import com.zqksk.api.domain.doctor.model.Doctor;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Table(name = "doctors")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DoctorEntity extends BaseEntity {
    @Column
    @Comment("의사 ID")
    private Long doctorId;

    @Column
    @Comment("의사 이름")
    private String name;

    @Column
    @Comment("의사 면허번호")
    private Long licenseNumber;

    @Column
    @Comment("병원 ID")
    private Long hospitalId;

    @Column
    @Comment("병원 이름")
    private String hospitalName;

    @Builder
    public DoctorEntity(Long doctorId, String name, Long licenseNumber, Long hospitalId, String hospitalName) {
        this.doctorId = doctorId;
        this.name = name;
        this.licenseNumber = licenseNumber;
        this.hospitalId = hospitalId;
        this.hospitalName = hospitalName;
    }

    public Doctor toDoctor() {
        return new Doctor(super.getId(), doctorId, name, licenseNumber, hospitalId, hospitalName);
    }
}
