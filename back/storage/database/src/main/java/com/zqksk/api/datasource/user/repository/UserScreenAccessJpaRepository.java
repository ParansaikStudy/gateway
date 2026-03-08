package com.zqksk.api.datasource.user.repository;

import com.zqksk.api.datasource.user.entity.UserScreenAccessEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserScreenAccessJpaRepository extends JpaRepository<UserScreenAccessEntity, Long> {
    List<UserScreenAccessEntity> findAllByUserId(Long userId);
}
