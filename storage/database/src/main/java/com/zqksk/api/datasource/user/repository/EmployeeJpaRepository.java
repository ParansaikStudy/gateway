package com.zqksk.api.datasource.user.repository;

import com.zqksk.api.datasource.user.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeJpaRepository extends JpaRepository<EmployeeEntity, Long> {
    EmployeeEntity findByNo(String employeeNo);
}
