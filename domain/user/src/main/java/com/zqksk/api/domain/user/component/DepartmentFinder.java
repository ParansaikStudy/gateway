package com.zqksk.api.domain.user.component;

import com.zqksk.api.domain.user.model.response.Department;
import com.zqksk.api.domain.user.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DepartmentFinder {

    private final DepartmentRepository departmentRepository;

    public Department getDepartmentByDepartmentCode(String departmentCode) {
        return departmentRepository.findByDepartmentCode(departmentCode);
    }
}
