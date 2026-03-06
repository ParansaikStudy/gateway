package com.zqksk.api.datasource.user.repository;

import com.zqksk.api.datasource.user.entity.DepartmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentJpaRepository extends JpaRepository<DepartmentEntity, Long> {
    DepartmentEntity findByCode(String departmentCode);
}
