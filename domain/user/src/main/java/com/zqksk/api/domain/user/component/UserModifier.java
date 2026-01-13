package com.zqksk.api.domain.user.component;

import com.zqksk.api.domain.user.model.request.CreateUser;
import com.zqksk.api.domain.user.model.response.User;
import com.zqksk.api.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserModifier {
    private final UserRepository userRepository;

    public User changePassword(String employeeNo, String hashPassword) {
        return userRepository.save(CreateUser.builder()
                .employeeNo(employeeNo)
                .password(hashPassword)
                .build());
    }
}
