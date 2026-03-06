package com.zqksk.api.datasource.user.entity;

import com.zqksk.api.datasource.BaseEntity;
import com.zqksk.api.domain.user.model.response.Department;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "departments")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class DepartmentEntity extends BaseEntity {
    @Size(max = 30)
    @Column(name = "code", length = 30)
    private String code;

    @Size(max = 500)
    @Column(name = "name", length = 500)
    private String name;

    @Size(max = 30)
    @Column(name = "upper_code", length = 30)
    private String upperCode;

    @Size(max = 60)
    @Column(name = "cost_center_code", length = 60)
    private String costCenterCode;

    @Size(max = 500)
    @Column(name = "cost_center_name", length = 500)
    private String costCenterName;

    @Column(name = "effective_start_date")
    private LocalDate effectiveStartDate;

    @Column(name = "effective_end_date")
    private LocalDate effectiveEndDate;

    @Size(max = 1)
    @Column(name = "effective_end_yn", length = 1)
    private String effectiveEndYn;

    @Column(name = "department_level_order")
    private Integer departmentLevelOrder;

    @Column(name = "sort_order")
    private Integer sortOrder;

    @Builder
    public DepartmentEntity(String code, String name, String upperCode, String costCenterCode, String costCenterName, LocalDate effectiveStartDate, LocalDate effectiveEndDate, String effectiveEndYn, Integer departmentLevelOrder, Integer sortOrder) {
        this.code = code;
        this.name = name;
        this.upperCode = upperCode;
        this.costCenterCode = costCenterCode;
        this.costCenterName = costCenterName;
        this.effectiveStartDate = effectiveStartDate;
        this.effectiveEndDate = effectiveEndDate;
        this.effectiveEndYn = effectiveEndYn;
        this.departmentLevelOrder = departmentLevelOrder;
        this.sortOrder = sortOrder;
    }

    public Department toDepartment() {
        return new Department(
                super.getId(),
                code,
                name,
                upperCode,
                costCenterCode,
                costCenterName,
                effectiveStartDate,
                effectiveEndDate,
                effectiveEndYn,
                departmentLevelOrder,
                sortOrder
        );
    }
}