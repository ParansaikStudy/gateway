package com.zqksk.api.datasource.user.repository;

import com.zqksk.api.datasource.user.entity.UserAuthEntity;
import com.zqksk.api.domain.user.model.NewUserAuth;
import com.zqksk.api.domain.user.model.request.CreateUserAuth;
import com.zqksk.api.domain.user.model.response.UserAuth;
import com.zqksk.api.domain.user.repository.UserAuthRepository;
import com.zqksk.api.exception.AuthenticationErrorType;
import com.zqksk.api.exception.JpaErrorType;
import com.zqksk.api.support.exception.CoreException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class InternalUserAuthRepository implements UserAuthRepository {
    private final UserAuthJpaRepository userAuthJpaRepository;

    public InternalUserAuthRepository(UserAuthJpaRepository userAuthJpaRepository) {
        this.userAuthJpaRepository = userAuthJpaRepository;
    }

    @Override
    public UserAuth save(NewUserAuth userAuth) {
        return userAuthJpaRepository.save(UserAuthEntity.builder()
                .roleId(userAuth.roleId())
                .userId(userAuth.userId())
                .build()
        ).toUserAuth();
    }

    @Override
    public void saveAll(List<CreateUserAuth> createUserAuths) {
        if(!createUserAuths.isEmpty()){
            List<UserAuthEntity> userAuthEntities = createUserAuths
                    .stream()
                    .map(dto -> {
                        UserAuthEntity userAuth = userAuthJpaRepository.findById(dto.id())
                                .orElseThrow(() -> new CoreException(JpaErrorType.NOT_FOUND_DATA));
                        userAuth.updateRoleId(
                                dto.roleId()
                        );
                        return userAuth;
                    }).toList();

            userAuthJpaRepository.saveAll(userAuthEntities);
        }
    }

    @Override
    public UserAuth findById(Long id) {
        return userAuthJpaRepository.findById(id)
                .orElseThrow(() -> new CoreException(AuthenticationErrorType.INVALID_USER))
                .toUserAuth();
    }

    @Override
    public UserAuth findByUserId(Long userId) {
        return userAuthJpaRepository.findByUserId(userId)
                .orElseThrow(() -> new CoreException(AuthenticationErrorType.INVALID_USER))
                .toUserAuth();
    }

    @Override
    public List<UserAuth> findAllByUserId(Long userId) {
        return userAuthJpaRepository.findAllByUserId(userId)
                .stream()
                .map(UserAuthEntity::toUserAuth)
                .toList();
    }
}
