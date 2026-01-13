package com.zqksk.api.domain.user.component;

import com.zqksk.api.domain.user.model.response.Employee;
import com.zqksk.api.domain.user.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmployeeFinder {

    private final EmployeeRepository employeeRepository;

    public Employee getEmployeeByEmployeeNo(String employeeNo) {
        return employeeRepository.findByEmployeeNo(employeeNo);
    }
}
