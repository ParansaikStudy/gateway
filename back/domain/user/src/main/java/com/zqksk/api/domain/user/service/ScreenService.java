package com.zqksk.api.domain.user.service;

import com.zqksk.api.domain.user.model.request.NewScreen;
import com.zqksk.api.domain.user.model.request.SearchTextRequestWithPage;
import com.zqksk.api.domain.user.model.response.Screen;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ScreenService {

    List<Screen> getScreenList();

    List<Screen> getRootScreenList();
    List<Screen> getScreenListWithCondition(String searchText);

    Page<Screen> getScreenListWithConditionAndPaging(SearchTextRequestWithPage searchTextRequestWithPage);

    Screen getScreen(Long id);

    Screen saveScreen(NewScreen createScreen);


}
