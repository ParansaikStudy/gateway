package com.zqksk.api.domain.user.repository;

import com.zqksk.api.domain.user.model.response.Department;

public interface DepartmentRepository {
    Department findByDepartmentCode(String departmentCode);
}
