package com.zqksk.api.datasource.user.repository;

import com.zqksk.api.datasource.user.entity.RoleEntity;
import com.zqksk.api.domain.user.model.response.Role;
import com.zqksk.api.domain.user.model.response.RoleType;
import com.zqksk.api.domain.user.repository.RoleRepository;
import com.zqksk.api.exception.JpaErrorType;
import com.zqksk.api.support.exception.CoreException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class RoleCoreRepository implements RoleRepository {

    private final RoleJpaRepository roleJpaRepository;

    @Override
    public List<Role> findAll() {
        return roleJpaRepository.findAll()
                .stream()
                .map(RoleEntity::toRole)
                .toList();
    }

    @Override
    public Role findByType(String type) {
        return roleJpaRepository.findByType(RoleType.from(type)).toRole();
    }

    @Override
    public Role findById(Long roleId) {
        return roleJpaRepository.findById(roleId)
                .orElseThrow(() -> new CoreException(JpaErrorType.NOT_FOUND_DATA))
                .toRole();
    }
}
