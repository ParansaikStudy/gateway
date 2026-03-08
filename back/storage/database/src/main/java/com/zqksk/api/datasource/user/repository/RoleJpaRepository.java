package com.zqksk.api.datasource.user.repository;

import com.zqksk.api.datasource.user.entity.RoleEntity;
import com.zqksk.api.domain.user.model.response.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleJpaRepository extends JpaRepository<RoleEntity, Long> {
    List<RoleEntity> findAllByType(RoleType type);
    RoleEntity findByType(RoleType type);
}
