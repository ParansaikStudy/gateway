package com.zqksk.api.domain.user.repository;

import com.zqksk.api.domain.user.model.NewUserAuth;
import com.zqksk.api.domain.user.model.request.CreateUserAuth;
import com.zqksk.api.domain.user.model.response.UserAuth;

import java.util.List;

public interface UserAuthRepository {
    UserAuth save(NewUserAuth userAuth);
    void saveAll(List<CreateUserAuth> createUserAuths);
    UserAuth findById(Long id);
    UserAuth findByUserId(Long userId);
    List<UserAuth> findAllByUserId(Long userId);
}
