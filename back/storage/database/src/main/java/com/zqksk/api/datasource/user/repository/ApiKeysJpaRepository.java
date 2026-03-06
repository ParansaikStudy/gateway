package com.zqksk.api.datasource.user.repository;

import com.zqksk.api.datasource.user.entity.ApiKeyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApiKeysJpaRepository extends JpaRepository<ApiKeyEntity, Long> {
    Optional<ApiKeyEntity> findByKey(String key);
}
