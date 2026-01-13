package com.zqksk.api.datasource.user.entity;

import com.zqksk.api.datasource.BaseEntity;
import com.zqksk.api.domain.user.model.response.Employee;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.time.LocalDate;

@Entity
@Table(name = "employee")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmployeeEntity extends BaseEntity {
    @Size(max = 60)
    @NotNull
    @Column(length = 60)
    @Comment("사원번호")
    private String no;
    @Size(max = 100)
    @Column(length = 100)
    @Comment("성명")
    private String name;
    @Size(max = 100)
    @Column(length = 100)
    @Comment("이메일")
    private String email;
    @Size(max = 100)
    @Column(length = 100)
    @Comment("전화번호")
    private String telephone;
    @Size(max = 100)
    @Column(length = 100)
    @Comment("핸드폰번호")
    private String mobile;
    @Column
    @Comment("입사일")
    private LocalDate enterDate;
    @Column
    @Comment("퇴사일")
    private LocalDate resignDate;
    @Column
    @Comment("부서배정시작일")
    private LocalDate departmentAssignStartDate;
    @Column
    @Comment("부서배정종료일")
    private LocalDate departmentAssignEndDate;
    @Size(max = 30)
    @Column(length = 30)
    @Comment("부서코드")
    private String departmentCode;
    @Size(max = 4)
    @Column(length = 4)
    @Comment("직책코드")
    private String dutyCode;
    @Size(max = 7)
    @Column(length = 7)
    @Comment("직무코드")
    private String businessDutyCode;
    @Size(max = 3)
    @Column(length = 3)
    @Comment("직위코드")
    private String positionCode;
    @Size(max = 6)
    @Column(length = 6)
    @Comment("직무그룹코드")
    private String jobGroupCode;
    @Size(max = 2)
    @Column(length = 2)
    @Comment("상태코드")
    private String statusCode;
    @Size(max = 1)
    @Column(length = 1)
    @Comment("겸직여부")
    private String concurrentPositionStatusYn;

    @Builder
    public EmployeeEntity(String no, String name, String email, String telephone, String mobile, LocalDate enterDate, LocalDate resignDate, LocalDate departmentAssignStartDate, LocalDate departmentAssignEndDate, String departmentCode, String dutyCode, String businessDutyCode, String positionCode, String jobGroupCode, String statusCode, String concurrentPositionStatusYn) {
        this.no = no;
        this.name = name;
        this.email = email;
        this.telephone = telephone;
        this.mobile = mobile;
        this.enterDate = enterDate;
        this.resignDate = resignDate;
        this.departmentAssignStartDate = departmentAssignStartDate;
        this.departmentAssignEndDate = departmentAssignEndDate;
        this.departmentCode = departmentCode;
        this.dutyCode = dutyCode;
        this.businessDutyCode = businessDutyCode;
        this.positionCode = positionCode;
        this.jobGroupCode = jobGroupCode;
        this.statusCode = statusCode;
        this.concurrentPositionStatusYn = concurrentPositionStatusYn;
    }

    public Employee toEmploy(){
        return new Employee(
                super.getId(),
                no,
                name,
                email,
                telephone,
                mobile,
                enterDate,
                resignDate,
                departmentAssignStartDate,
                departmentAssignEndDate,
                departmentCode,
                dutyCode,
                businessDutyCode,
                positionCode,
                jobGroupCode,
                statusCode,
                concurrentPositionStatusYn
        );
    }


}
