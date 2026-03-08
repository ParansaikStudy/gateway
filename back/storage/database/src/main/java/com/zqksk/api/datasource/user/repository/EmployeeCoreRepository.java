package com.zqksk.api.datasource.user.repository;

import com.zqksk.api.domain.user.model.response.Employee;
import com.zqksk.api.domain.user.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class EmployeeCoreRepository implements EmployeeRepository {
    private final EmployeeJpaRepository employeeJpaRepository;

    @Override
    public Employee findByEmployeeNo(String employeeNo) {
        return employeeJpaRepository.findByNo(employeeNo).toEmploy();
    }
}
