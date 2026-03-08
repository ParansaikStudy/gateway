package com.zqksk.api.domain.user.component;

import com.zqksk.api.domain.user.model.request.CreateRoleScreensAccess;
import com.zqksk.api.domain.user.repository.RoleScreensAccessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccessScreensAppender {

    private final RoleScreensAccessRepository roleScreensAccessRepository;

    public void append(CreateRoleScreensAccess createRoleScreensAccess){
        roleScreensAccessRepository.save(createRoleScreensAccess);
    }
}
