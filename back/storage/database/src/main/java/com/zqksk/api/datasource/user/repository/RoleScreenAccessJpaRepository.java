package com.zqksk.api.datasource.user.repository;

import com.zqksk.api.datasource.user.entity.RoleScreenAccessEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleScreenAccessJpaRepository extends JpaRepository<RoleScreenAccessEntity, Long> {
    List<RoleScreenAccessEntity> findAllByRoleId(Long roleId);
}
