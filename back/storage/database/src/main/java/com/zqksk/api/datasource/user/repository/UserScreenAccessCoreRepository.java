package com.zqksk.api.datasource.user.repository;

import com.zqksk.api.datasource.user.entity.*;
import com.zqksk.api.domain.user.model.response.UserAndScreens;
import com.zqksk.api.domain.user.model.response.UserScreenAccess;
import com.zqksk.api.domain.user.repository.UserScreenAccessRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static com.zqksk.api.datasource.user.entity.QRoleEntity.roleEntity;
import static com.zqksk.api.datasource.user.entity.QUserAuthEntity.userAuthEntity;
import static com.zqksk.api.datasource.user.entity.QUserEntity.userEntity;

@Repository
@RequiredArgsConstructor
public class UserScreenAccessCoreRepository implements UserScreenAccessRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final UserScreenAccessJpaRepository userScreenAccessJpaRepository;
    @Override
    public List<UserAndScreens> findAllWithCondition(String searchText) {

        return jpaQueryFactory.select(userEntity,userAuthEntity,roleEntity)
                .from(userEntity)
                .leftJoin(userAuthEntity)
                .on(userEntity.id.eq(userAuthEntity.userId))
                .leftJoin(roleEntity)
                .on(userAuthEntity.roleId.eq(roleEntity.id))
                .where(searchUser(searchText))
                .fetch()
                .stream()
                .map(tuple -> {
                    UserEntity user = tuple.get(userEntity);
                    UserAuthEntity userAuth = tuple.get(userAuthEntity);

                    return new UserAndScreens(
                            user != null ? user.toUser(): null,
                            userAuth != null ? userAuth.toUserAuth() : null,
                            null
                    );
                })
                .collect(Collectors.toList());

    }

    @Override
    public Page<UserAndScreens> findAllWithConditionAndPage(int page, int size, String searchText) {

        PageRequest pageRequest = PageRequest.of(page, size);

        List<UserAndScreens> results =  jpaQueryFactory.select(userEntity,userAuthEntity,roleEntity)
                .from(userEntity)
                .leftJoin(userAuthEntity)
                .on(userEntity.id.eq(userAuthEntity.userId))
                .leftJoin(roleEntity)
                .on(userAuthEntity.roleId.eq(roleEntity.id))
                .where(searchUser(searchText))
                .orderBy(userEntity.id.desc())
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch()
                .stream()
                .map(tuple -> {
                    UserEntity user = tuple.get(userEntity);
                    UserAuthEntity userAuth = tuple.get(userAuthEntity);

                    return new UserAndScreens(
                            user != null ? user.toUser(): null,
                            userAuth != null ? userAuth.toUserAuth() : null,
                            null
                    );
                })
                .toList();


        Long totalCount = jpaQueryFactory.select(userEntity.count())
                .from(userEntity)
                .leftJoin(userAuthEntity)
                .on(userEntity.id.eq(userAuthEntity.userId))
                .leftJoin(roleEntity)
                .on(userAuthEntity.roleId.eq(roleEntity.id))
                .where(searchUser(searchText))
                .fetchOne();

        long total = totalCount != null ? totalCount : 0L;

        return new PageImpl<>(results, pageRequest, total);
    }

    @Override
    public List<UserScreenAccess> findScreensByUserId(Long userId) {
        return userScreenAccessJpaRepository.findAllByUserId(userId)
                .stream()
                .map(UserScreenAccessEntity::toUserScreenAccess)
                .toList();
    }

    private BooleanBuilder searchUser(String searchText){

        BooleanBuilder builder = new BooleanBuilder();

        if(!isEmptySearchText(searchText)){
            builder.or(roleEntity.name.containsIgnoreCase(searchText));
            builder.or(userEntity.employeeNo.containsIgnoreCase(searchText));
            builder.or(userEntity.departmentName.containsIgnoreCase(searchText));
            builder.or(userEntity.positionName.containsIgnoreCase(searchText));
            builder.or(userEntity.name.containsIgnoreCase(searchText));
            builder.or(userEntity.email.containsIgnoreCase(searchText));
        }

        return builder;
    }


    private boolean isEmptySearchText(String searchText){
        return searchText == null || searchText.isBlank();
    }

}
