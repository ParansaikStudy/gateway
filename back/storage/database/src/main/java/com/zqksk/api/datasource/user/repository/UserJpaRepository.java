package com.zqksk.api.datasource.user.repository;

import com.zqksk.api.datasource.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmployeeNo(String employeeNo);
}
