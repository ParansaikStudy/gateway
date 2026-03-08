package com.zqksk.api.domain.user.component;

import com.zqksk.api.domain.user.model.NewUserAuth;
import com.zqksk.api.domain.user.model.request.CreateUserAuth;
import com.zqksk.api.domain.user.model.response.UserAuth;
import com.zqksk.api.domain.user.repository.UserAuthRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserAuthAppender {
    private final UserAuthRepository userAuthRepository;

    public UserAuthAppender(UserAuthRepository userAuthRepository) {
        this.userAuthRepository = userAuthRepository;
    }

    public UserAuth append(Long roleId, Long userId) {
        NewUserAuth newUserAuth = new NewUserAuth(roleId, userId);
        return userAuthRepository.save(newUserAuth);
    }

    public void appendList(List<CreateUserAuth> createUserAuths){
        userAuthRepository.saveAll(createUserAuths);
    }
}
