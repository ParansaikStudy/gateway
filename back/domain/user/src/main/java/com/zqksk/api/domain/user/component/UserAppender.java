package com.zqksk.api.domain.user.component;

import com.zqksk.api.domain.user.model.request.CreateUser;
import com.zqksk.api.domain.user.model.response.User;
import com.zqksk.api.domain.user.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class UserAppender {
    private final UserRepository userRepository;

    public UserAppender(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User append(CreateUser createUser) {
        return userRepository.save(createUser);
    }
}
