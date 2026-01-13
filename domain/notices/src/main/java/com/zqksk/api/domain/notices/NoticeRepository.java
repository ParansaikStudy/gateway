package com.zqksk.api.domain.notices;

import org.springframework.data.domain.Page;

import java.util.List;

public interface NoticeRepository {

    Notices save(final NewNotices newNotices);

    Notices update(final NewNotices newNotices);

    Long deleteById(Long id);

    Long deleteAllById(List<Long> idList);

    Long changeIndexById(Long id, Long index);

    Long clearIndexForExpiredNotices();

    List<Notices> findAll();

    Page<Notices> findAllWithConditionAndPaging(int page, int size, String searchText, Integer pgType);

    List<Notices> findAllByPgType(Integer pgType);

    Long findLastIndex();

    Notices findById(Long id);

    List<Notices> findAllActiveNoticesWithoutIndex();


}
