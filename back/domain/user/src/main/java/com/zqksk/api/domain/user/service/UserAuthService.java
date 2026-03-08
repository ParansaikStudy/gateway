package com.zqksk.api.domain.user.service;

import com.zqksk.api.domain.user.component.*;
import com.zqksk.api.domain.user.model.response.*;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class UserAuthService {
    private final UserAuthFinder userAuthFinder;
    private final UserAuthAppender userAuthAppender;
    private final ApiKeyFinder apiKeyFinder;
    private final UserFinder userFinder;
    private final RoleFinder roleFinder;
    private final AccessScreensFinder accessScreensFinder;
    private final ScreenFinder screenFinder;

    @Cacheable(key = "#key != null ? #key : 'NULL'", cacheNames = "users")
    public Boolean validateKey(String key) {
        return apiKeyFinder.getApiKeyByKey(key).id() != null;
    }

    public UserPrivileges getUserPrivileges(String employeeNo) {
        User user = userFinder.getUserByEmployeeNo(employeeNo);
        List<UserAuth> userAuths = userAuthFinder.getUserAuthsByUserId(user.id());
        List<Role> roles = userAuths.stream()
                .map(userAuth -> roleFinder.getRoleById(userAuth.roleId()))
                .toList();

        List<Long> screenIds = userAuths.stream()
                .flatMap(userAuth -> accessScreensFinder.getScreensByRole(userAuth.roleId()).stream())
                .map(RoleScreenAccess::screenId)
                .distinct()
                .toList();

        List<Long> userAccessScreenIds = userAuths.stream()
                .flatMap(userAuth -> accessScreensFinder.getScreensByUserId(userAuth.userId()).stream())
                .map(UserScreenAccess::screenId)
                .distinct()
                .toList();

        List<Screen> screens = Stream.concat(
                        screenIds.stream(),
                        userAccessScreenIds.stream()
                )
                .distinct()
                .toList().stream()
                .sorted()
                .map(screenFinder::getScreenById)
                .toList();

        List<Long> screenParentIds = screens.stream().map(Screen::id).toList();

        List<Screen> childScreens = screenFinder.getScreenListByParentIds(screenParentIds);

        List<ScreenTree> screenTrees = buildScreenTree(screens, childScreens);


        return new UserPrivileges(user, roles, screenTrees);
    }

    private List<ScreenTree> buildScreenTree(List<Screen> rootScreens, List<Screen> childScreens){
       Map<Long, List<Screen>> childMap = childScreens.stream()
               .collect(Collectors.groupingBy(Screen::parentId));


       return rootScreens.stream()
               .map(root -> buildTreeNode(root, childMap))
               .toList();

    }

    private ScreenTree buildTreeNode(Screen screen, Map<Long, List<Screen>> childMap){
        List<ScreenTree> child = childMap.getOrDefault(screen.id(), Collections.emptyList())
                .stream()
                .map(childScreen -> buildTreeNode(childScreen, childMap))
                .toList();

        return new ScreenTree(
                screen.id(),
                screen.parentId(),
                screen.name(),
                screen.componentName(),
                screen.url(),
                child);
    }


}
