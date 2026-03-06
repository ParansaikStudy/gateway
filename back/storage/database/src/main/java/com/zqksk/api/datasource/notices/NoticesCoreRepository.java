package com.zqksk.api.datasource.notices;

import com.zqksk.api.domain.notices.*;
import com.zqksk.api.exception.JpaErrorType;
import com.zqksk.api.support.exception.CoreException;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.time.*;
import java.util.List;

import static com.zqksk.api.datasource.notices.QNoticesEntity.noticesEntity;

@Repository
@RequiredArgsConstructor
public class NoticesCoreRepository implements NoticeRepository {

    private final NoticesJpaRepository noticesJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Notices save(NewNotices newNotices) {
        return noticesJpaRepository.save(
                NoticesEntity.builder()
                        .pgType(newNotices.pgType())
                        .title(newNotices.title())
                        .content(newNotices.content())
                        .noticeStartDate(newNotices.noticeStartDate())
                        .noticeEndDate(newNotices.noticeEndDate())
                        .userId(newNotices.userId())
                        .index(newNotices.index())
                        .build()
        ).toNotices();
    }

    @Override
    public Notices update(NewNotices newNotices) {

        NoticesEntity notices = noticesJpaRepository.findById(newNotices.id())
                .orElseThrow(() -> new CoreException(JpaErrorType.NOT_FOUND_DATA));

        notices.update(
                newNotices.title(),
                newNotices.pgType(),
                newNotices.content(),
                newNotices.noticeStartDate(),
                newNotices.noticeEndDate(),
                newNotices.userId(),
                newNotices.index());


        return noticesJpaRepository.save(notices).toNotices();
    }

    @Override
    public Long clearIndexForExpiredNotices() {

        Instant now = Instant.now();
        LocalDate today = now.atZone(ZoneId.of("UTC")).toLocalDate();

        Long count = new JPAUpdateClause(entityManager,noticesEntity)
                .set(noticesEntity.index, (Long) null)
                .where(
                        noticesEntity.noticeStartDate.gt(today)
                        .or(noticesEntity.noticeEndDate.lt(today))
                                .and(noticesEntity.index.isNotNull())
                ).execute();

        entityManager.clear();

        return count;
    }



    @Override
    public List<Notices> findAll() {
        List<NoticesEntity> noticesEntities = noticesJpaRepository.findAll();

        return noticesEntities.stream()
                .map(NoticesEntity::toNotices)
                .toList();

    }

    @Override
    public Page<Notices> findAllWithConditionAndPaging(int page, int size, String searchText, Integer pgType) {
        PageRequest pageRequest = PageRequest.of(page, size);

        List<Notices> noticesList = jpaQueryFactory.selectFrom(noticesEntity)
                .where(containSearchText(searchText), containPgType(pgType), isNotDelete())
                .orderBy(
                        indexOrder(),  // index 정렬 (오름차순 & null 값은 마지막)
                        idOrder()      // index가 null이면 id 기준 내림차순
                )
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch().stream().map(NoticesEntity::toNotices)
                .toList();

        Long totalCount = jpaQueryFactory.select(noticesEntity.count())
                .from(noticesEntity)
                .where(containSearchText(searchText), containPgType(pgType), isNotDelete())
                .fetchOne();

        long total = totalCount != null ? totalCount : 0L;

        return new PageImpl<>(noticesList, pageRequest, total);
    }

    @Override
    public List<Notices> findAllByPgType(Integer pgType) {
        return jpaQueryFactory.selectFrom(noticesEntity)
                .where(noticesEntity.pgType.eq(pgType).and(noticesEntity.index.isNotNull()).and(isNotDelete()) )
                .orderBy(indexOrder())
                .fetch().stream()
                .map(NoticesEntity::toNotices)
                .toList();
    }

    @Override
    public Long findLastIndex() {
       Long lastIndex =  jpaQueryFactory.select(noticesEntity.index.max())
                .from(noticesEntity)
               .fetchOne();

       return lastIndex != null ? lastIndex : 0;
    }

    @Override
    public Notices findById(Long id) {
        return noticesJpaRepository.findById(id)
                .orElseThrow(() -> new CoreException(JpaErrorType.NOT_FOUND_DATA))
                .toNotices();
    }

    @Override
    public List<Notices> findAllActiveNoticesWithoutIndex() {

        Instant now = Instant.now();
        LocalDate today = now.atZone(ZoneId.of("UTC")).toLocalDate();

        return jpaQueryFactory.selectFrom(noticesEntity)
                .where(
                        noticesEntity.noticeStartDate.loe(today)
                                .and(noticesEntity.noticeEndDate.goe(today))
                                .and(noticesEntity.index.isNull())
                )
                .orderBy(noticesEntity.noticeStartDate.asc())
                .fetch().stream().map(NoticesEntity::toNotices)
                .toList();
    }

    @Override
    public Long deleteById(Long id) {

        return jpaQueryFactory.update(noticesEntity)
                .set(noticesEntity.deleteYn,"Y").set(noticesEntity.index,  (Long) null)
                .where(noticesEntity.id.eq(id))
                .execute();
    }

    @Override
    public Long deleteAllById(List<Long> idList) {
        return jpaQueryFactory.update(noticesEntity)
                .set(noticesEntity.deleteYn,"Y")
                .set(noticesEntity.index,  (Long) null)
                .where(noticesEntity.id.in(idList))
                .execute();
    }

    @Override
    public Long changeIndexById(Long id, Long index) {
        return jpaQueryFactory.update(noticesEntity)
                .set(noticesEntity.index, index)
                .where(noticesEntity.id.eq(id))
                .execute();
    }

    private BooleanExpression containSearchText(String searchText){
        if(searchText == null || searchText.isBlank()){
            return null;
        }

        return noticesEntity.title.containsIgnoreCase(searchText);
    }

    private BooleanExpression containPgType(Integer pgType){
        if(pgType == null){
            return  null;
        }

        return noticesEntity.pgType.eq(pgType);
    }


    private BooleanExpression isNotDelete(){
        return noticesEntity.deleteYn.isNull();
    }

    private OrderSpecifier<Long> indexOrder(){
        return noticesEntity.index.asc().nullsLast();
    }

    private OrderSpecifier<Long> idOrder(){
        return noticesEntity.id.desc();
    }
}
