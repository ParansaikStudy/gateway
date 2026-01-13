package com.zqksk.api.domain.user.repository;

import com.zqksk.api.domain.user.model.request.NewScreen;
import com.zqksk.api.domain.user.model.response.Screen;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ScreenRepository {
    Screen findById(Long screenId);

    Screen save(final NewScreen screen);

    List<Screen> findAll();

    List<Screen> findRootList();

    List<Screen> findAllWithCondition(String searchText);

    Page<Screen> findAllWithConditionAndPaging(int page, int size, String searchText);

    List<Screen> findAllByParentIds(List<Long> idList);
}
