package com.zqksk.api.datasource.user.repository;

import com.zqksk.api.datasource.user.entity.UserAuthEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserAuthJpaRepository extends JpaRepository<UserAuthEntity, Long> {
    Optional<UserAuthEntity> findByUserId(Long userId);
    List<UserAuthEntity> findAllByUserId(Long userId);
}
