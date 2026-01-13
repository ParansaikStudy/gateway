package com.zqksk.api.domain.user.service;

import com.zqksk.api.domain.user.model.request.CreateUserAuth;

import java.util.List;

public interface InternalUserAuthService {


    void saveAll(List<CreateUserAuth> createUserAuthList);
}
