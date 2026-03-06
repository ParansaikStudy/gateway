package com.zqksk.api.datasource.user.repository;

import com.zqksk.api.datasource.user.entity.ScreenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ScreenJpaRepository extends JpaRepository<ScreenEntity, Long> {

    Optional<ScreenEntity> findByParentId(Long parentId);
}
