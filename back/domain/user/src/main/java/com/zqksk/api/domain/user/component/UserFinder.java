package com.zqksk.api.domain.user.component;

import com.zqksk.api.domain.user.model.response.User;
import com.zqksk.api.domain.user.model.response.UserLogin;
import com.zqksk.api.domain.user.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class UserFinder {
    private final UserRepository userRepository;

    public UserFinder(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserByEmployeeNo(String employeeNo) {
        return userRepository.findByEmployeeNo(employeeNo);
    }

    public UserLogin getUserPasswordByEmployeeNo(String employeeNo) {
        return userRepository.findLoginInfoByEmployeeNo(employeeNo);
    }


    public User getUserByUserId(Long userId) { return  userRepository.findById(userId);}




}
