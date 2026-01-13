package com.zqksk.api.domain.user.service;

import com.zqksk.api.domain.user.model.request.CreateRoleScreensAccess;
import com.zqksk.api.domain.user.model.request.SearchTextRequestWithPage;
import com.zqksk.api.domain.user.model.response.RoleScreenAccess;
import com.zqksk.api.domain.user.model.response.UserAndScreens;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AccessScreensService {


    List<RoleScreenAccess> getScreensByRole(Long roleId);

    default List<UserAndScreens> getScreensWithCondition(String searchText){
        throw new UnsupportedOperationException("Not implemented");
    }

    default Page<UserAndScreens> getScreensWithConditionAndPage(SearchTextRequestWithPage searchUserRequestWithPage){
        throw new UnsupportedOperationException("Not implemented");
    }

    void saveRoleScreensAccess(CreateRoleScreensAccess roleScreensAccessRequest);

}
