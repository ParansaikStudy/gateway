package com.zqksk.api.domain.user.repository;

import com.zqksk.api.domain.user.model.response.UserAndScreens;
import com.zqksk.api.domain.user.model.response.UserScreenAccess;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserScreenAccessRepository {

    List<UserAndScreens> findAllWithCondition(String searchText);

    Page<UserAndScreens> findAllWithConditionAndPage(int page, int size, String searchText);

    List<UserScreenAccess> findScreensByUserId(Long userId);
}
