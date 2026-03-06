package com.zqksk.api.domain.user.repository;

import com.zqksk.api.domain.user.model.request.CreateRoleScreensAccess;
import com.zqksk.api.domain.user.model.response.RoleScreenAccess;

import java.util.List;

public interface RoleScreensAccessRepository {

    List<RoleScreenAccess> findScreensByRole(Long roleId);

    void save(CreateRoleScreensAccess createRoleScreensAccess);






}
