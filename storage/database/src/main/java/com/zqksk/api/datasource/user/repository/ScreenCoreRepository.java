package com.zqksk.api.datasource.user.repository;

import com.zqksk.api.datasource.user.entity.ScreenEntity;
import com.zqksk.api.domain.user.model.request.NewScreen;
import com.zqksk.api.domain.user.model.response.Screen;
import com.zqksk.api.domain.user.repository.ScreenRepository;
import com.zqksk.api.exception.JpaErrorType;
import com.zqksk.api.support.exception.CoreException;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.zqksk.api.datasource.user.entity.QScreenEntity.screenEntity;

@Repository
@RequiredArgsConstructor
public class ScreenCoreRepository implements ScreenRepository {

    private final ScreenJpaRepository screenJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Screen findById(Long screenId) {
        return screenJpaRepository.findById(screenId)
                .orElseThrow(() -> new CoreException(JpaErrorType.NOT_FOUND_DATA))
                .toScreen();
    }


    @Override
    public List<Screen> findAllWithCondition(String searchText) {

        return jpaQueryFactory.selectFrom(screenEntity)
                .where(containSearchText(searchText))
                .fetch()
                .stream()
                .map(ScreenEntity::toScreen)
                .toList();
    }

    @Override
    public Page<Screen> findAllWithConditionAndPaging(int page, int size, String searchText) {
        PageRequest pageRequest = PageRequest.of(page,size);

        List<Screen> screenList = jpaQueryFactory.selectFrom(screenEntity)
                .where(containSearchText(searchText))
                .orderBy(screenEntity.id.desc())
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch()
                .stream()
                .map(ScreenEntity::toScreen)
                .toList();

        Long totalCount = jpaQueryFactory.select(screenEntity.count())
                .from(screenEntity)
                .where(containSearchText(searchText))
                .fetchOne();

        long total = totalCount != null ? totalCount : 0L;

        return new PageImpl<>(screenList, pageRequest, total);
    }

    @Override
    public List<Screen> findAllByParentIds(List<Long> idList) {
        return jpaQueryFactory.selectFrom(screenEntity)
                .where(screenEntity.parentId.in(idList))
                .fetch()
                .stream()
                .map(ScreenEntity::toScreen)
                .toList();
    }

    @Override
    public Screen save(NewScreen screen) {

        ScreenEntity screenEntityResult;

        if(screen.parentId() != null){
            screenJpaRepository.findByParentId(screen.parentId())
                    .orElseThrow(() -> new CoreException(JpaErrorType.NOT_FOUND_DATA));

        }

        if(screen.id() == null){
            screenEntityResult = ScreenEntity.builder()
                    .parentId(screen.parentId())
                    .name(screen.name())
                    .componentName(screen.componentName())
                    .url(screen.url())
                    .build();

        }else{
            screenEntityResult = screenJpaRepository.findById(screen.id())
                    .orElseThrow(() -> new CoreException(JpaErrorType.NOT_FOUND_DATA));

            screenEntityResult.update(
                    screen.parentId(),
                    screen.name(),
                    screen.componentName(),
                    screen.url()
            );

        }

        return screenJpaRepository.save(screenEntityResult).toScreen();
    }

    @Override
    public List<Screen> findAll() {
        return screenJpaRepository.findAll().stream().map(ScreenEntity::toScreen).toList();
    }

    @Override
    public List<Screen> findRootList() {
        return jpaQueryFactory.selectFrom(screenEntity)
                .where(screenEntity.parentId.isNull())
                .fetch()
                .stream()
                .map(ScreenEntity::toScreen)
                .toList();
    }

    private BooleanExpression containSearchText(String searchText){
        if(searchText == null || searchText.isBlank()){
            return null;
        }
        return screenEntity.name.containsIgnoreCase(searchText);
    }
}
