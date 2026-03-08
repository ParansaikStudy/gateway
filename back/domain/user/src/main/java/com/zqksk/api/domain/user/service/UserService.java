package com.zqksk.api.domain.user.service;

import com.zqksk.api.domain.user.component.UserAppender;
import com.zqksk.api.domain.user.component.UserFinder;
import com.zqksk.api.domain.user.model.request.CreateUser;
import com.zqksk.api.domain.user.model.response.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserFinder userFinder;
    private final UserAppender userAppender;



    public User signUp(CreateUser createUser) {
        return userAppender.append(createUser);
    }

    private User getUserById(Long id){return userFinder.getUserByUserId(id);}


}
