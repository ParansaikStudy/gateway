package com.zqksk.api.domain.user.repository;

import com.zqksk.api.domain.user.model.request.CreateUser;
import com.zqksk.api.domain.user.model.response.User;
import com.zqksk.api.domain.user.model.response.UserLogin;

public interface UserRepository {
    User save(CreateUser createUser);
    User findByEmployeeNo(String employeeNo);
    UserLogin findLoginInfoByEmployeeNo(String employeeNo);

    User findById(Long userId);
}
