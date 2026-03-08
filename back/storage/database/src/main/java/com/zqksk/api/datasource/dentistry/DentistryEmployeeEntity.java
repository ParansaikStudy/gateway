package com.zqksk.api.datasource.dentistry;

import com.zqksk.api.datasource.BaseEntity;
import com.zqksk.api.domain.dentistry.Dentistry;
import com.zqksk.api.domain.dentistry.DentistryEmployee;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;
import java.util.Base64;

@Getter
@Entity
@Table(name = "dentistry_employee")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DentistryEmployeeEntity extends BaseEntity {
    @Size(max = 15)
    @NotNull
    @Comment("요양기관번호")
    @Column(name = "hospital_id", nullable = false, length = 15)
    private String hospitalId;

    @Column
    @Comment("프로그램 구분(0: 두번에, 1: 하나로, 2: OneClick, 3: OneCodi, 4: OneMessenger, 5: OneServer, 6: One2  7" +
            ": One3 ,8: V-ceph, 9: OneClickM, 10: OneDeskM, 11: OnePhoto, 12: OneChartScanM, 13: OneCodiM , 14: DBMigration)")
    private int pgType;

    @Size(max = 50)
    @Comment("직원 번호")
    @Column(name = "employee_no", length = 50)
    private String employeeNo;

    @Size(max = 20)
    @Comment("직원 이름")
    @Column(name = "employee_name", length = 20)
    private String employeeName;

    @Size(max = 50)
    @Comment("진료과")
    @Column(name = "medical_specialty", length = 20)
    private String medicalSpecialty;

    @Lob
    @Comment("프로필 사진")
    @Column(name = "profile", columnDefinition = "BLOB")
    private byte[] profile;

    @Builder
    public DentistryEmployeeEntity(String hospitalId, int pgType, String employeeNo, String employeeName, String medicalSpecialty, byte[] profile) {
        this.hospitalId = hospitalId;
        this.pgType = pgType;
        this.employeeNo = employeeNo;
        this.employeeName = employeeName;
        this.medicalSpecialty = medicalSpecialty;
        this.profile = profile;
    }

    public DentistryEmployeeEntity update(String hospitalId, int pgType, String employeeNo, String employeeName, String medicalSpecialty, byte[] profile) {
        this.hospitalId = hospitalId;
        this.pgType = pgType;
        this.employeeNo = employeeNo;
        this.employeeName = employeeName;
        this.medicalSpecialty = medicalSpecialty;
        this.profile = profile;
        return this;
    }

    public DentistryEmployee toDentistryEmployee() {
        String profileBase64 = null;
        if(profile != null) {
            profileBase64 = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(profile);
        }
        return DentistryEmployee.builder()
                .id(getId())
                .hospitalId(hospitalId)
                .pgType(pgType)
                .employeeNo(employeeNo)
                .employeeName(employeeName)
                .medicalSpecialty(medicalSpecialty)
                .profile(profileBase64)
                .build();
    }
}