package com.zqksk.api.domain.user.repository;

import com.zqksk.api.domain.user.model.response.Employee;

public interface EmployeeRepository {
    Employee findByEmployeeNo(String employeeNo);
}
