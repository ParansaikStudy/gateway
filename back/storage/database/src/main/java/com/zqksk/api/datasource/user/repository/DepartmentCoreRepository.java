package com.zqksk.api.datasource.user.repository;

import com.zqksk.api.datasource.user.entity.DepartmentEntity;
import com.zqksk.api.domain.user.model.response.Department;
import com.zqksk.api.domain.user.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DepartmentCoreRepository implements DepartmentRepository {

    private final DepartmentJpaRepository departmentJpaRepository;

    @Override
    public Department findByDepartmentCode(String departmentCode) {
        return departmentJpaRepository.findByCode(departmentCode).toDepartment();
    }
}
