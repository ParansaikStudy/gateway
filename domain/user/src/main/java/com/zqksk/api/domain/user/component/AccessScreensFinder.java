package com.zqksk.api.domain.user.component;

import com.zqksk.api.domain.user.model.request.SearchTextRequestWithPage;
import com.zqksk.api.domain.user.model.response.RoleScreenAccess;
import com.zqksk.api.domain.user.model.response.UserAndScreens;
import com.zqksk.api.domain.user.model.response.UserScreenAccess;
import com.zqksk.api.domain.user.repository.RoleScreensAccessRepository;
import com.zqksk.api.domain.user.repository.UserScreenAccessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AccessScreensFinder {


    private final RoleScreensAccessRepository roleScreensAccessRepository;
    private final UserScreenAccessRepository userScreenAccessRepository;

    public List<RoleScreenAccess>  getScreensByRole(Long roleId){
        return roleScreensAccessRepository.findScreensByRole(roleId);
    }

    public List<UserScreenAccess>  getScreensByUserId(Long userId){
        return userScreenAccessRepository.findScreensByUserId(userId);
    }

    public List<UserAndScreens> getScreensWithCondition(String searchText){
        return userScreenAccessRepository.findAllWithCondition(searchText);
    }

    public Page<UserAndScreens> getScreensWithConditionAndPage(SearchTextRequestWithPage searchUserRequestWithPage){
        return userScreenAccessRepository.findAllWithConditionAndPage(searchUserRequestWithPage.page(), searchUserRequestWithPage.size(), searchUserRequestWithPage.searchText());
    }
}
