package com.zqksk.api.datasource.user.repository;

import com.zqksk.api.datasource.user.entity.RoleScreenAccessEntity;
import com.zqksk.api.domain.user.model.request.CreateRoleScreensAccess;
import com.zqksk.api.domain.user.model.response.RoleScreenAccess;
import com.zqksk.api.domain.user.repository.RoleScreensAccessRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.zqksk.api.datasource.user.entity.QRoleScreenAccessEntity.roleScreenAccessEntity;

@Repository
@RequiredArgsConstructor
public class RoleScreenAccessCoreRepository implements RoleScreensAccessRepository {

    private final RoleScreenAccessJpaRepository roleScreenAccessJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public List<RoleScreenAccess> findScreensByRole(Long roleId) {

        return roleScreenAccessJpaRepository.findAllByRoleId(roleId)
                .stream()
                .map(RoleScreenAccessEntity::toRoleScreenAccess)
                .toList();

    }

    @Override
    public void save(CreateRoleScreensAccess createRoleScreensAccess) {

        jpaQueryFactory.delete(roleScreenAccessEntity)
                .where(roleScreenAccessEntity.roleId.eq(createRoleScreensAccess.roleId()))
                .execute();

        if(!createRoleScreensAccess.screenIdList().isEmpty()){
            List<RoleScreenAccessEntity> screenEntities = createRoleScreensAccess.screenIdList()
                    .stream()
                    .map(screenId -> new RoleScreenAccessEntity(
                            createRoleScreensAccess.roleId(),
                            screenId
                    )).toList();

            roleScreenAccessJpaRepository.saveAll(screenEntities);
        }


    }
}
