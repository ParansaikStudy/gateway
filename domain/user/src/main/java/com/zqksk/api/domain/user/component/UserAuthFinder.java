package com.zqksk.api.domain.user.component;

import com.zqksk.api.domain.user.model.response.UserAuth;
import com.zqksk.api.domain.user.repository.UserAuthRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserAuthFinder {
    private final UserAuthRepository userAuthRepository;

    public UserAuthFinder(UserAuthRepository userAuthRepository) {
        this.userAuthRepository = userAuthRepository;
    }

    public UserAuth getKeyById(Long id) {
        return userAuthRepository.findById(id);
    }

    public UserAuth getUserAuthByUserId(Long userId) {
        return userAuthRepository.findByUserId(userId);
    }

    public List<UserAuth> getUserAuthsByUserId(Long userId) {
        return userAuthRepository.findAllByUserId(userId);
    }
}
