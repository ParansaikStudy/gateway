package com.zqksk.api.datasource.user.repository;

import com.zqksk.api.datasource.user.entity.UserEntity;
import com.zqksk.api.domain.user.model.request.CreateUser;
import com.zqksk.api.domain.user.model.response.User;
import com.zqksk.api.domain.user.model.response.UserLogin;
import com.zqksk.api.domain.user.repository.UserRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CoreUserRepository implements UserRepository {
    private final UserJpaRepository userJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public User save(CreateUser createUser) {
        UserEntity userEntity = userJpaRepository.findByEmployeeNo(createUser.employeeNo()).orElse(null);
        if(userEntity != null) {
            return userJpaRepository.save(userEntity.updatePassword(createUser.password())
            ).toUser();
        }

        return userJpaRepository.save(UserEntity.builder()
                .employeeNo(createUser.employeeNo())
                .name(createUser.name())
                .email(createUser.email())
                .departmentName(createUser.departmentName())
                .positionName(createUser.positionName())
                .password(createUser.password())
                .build()
        ).toUser();
    }

    @Override
    public User findByEmployeeNo(String employeeNo) {
        return userJpaRepository.findByEmployeeNo(employeeNo)
                .map(UserEntity::toUser)
                .orElse(null);
    }

    @Override
    public UserLogin findLoginInfoByEmployeeNo(String employeeNo) {
        return userJpaRepository.findByEmployeeNo(employeeNo)
                .map(UserEntity::toUserLogin)
                .orElse(null);
    }

    @Override
    public User findById(Long userId) {
        return userJpaRepository.findById(userId)
                .map(UserEntity::toUser)
                .orElse(null);
    }
}
