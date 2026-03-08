package com.zqksk.api.domain.user.repository;

import com.zqksk.api.domain.user.model.response.Role;

import java.util.List;

public interface RoleRepository {

    List<Role> findAll();
    Role findByType(String type);
    Role findById(Long userId);
}
