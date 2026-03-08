package com.zqksk.api.domain.user.component;

import com.zqksk.api.domain.user.model.request.SearchTextRequestWithPage;
import com.zqksk.api.domain.user.model.response.Screen;
import com.zqksk.api.domain.user.repository.ScreenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ScreenFinder {

    private final ScreenRepository screenRepository;

    public Screen getScreenById(Long id) {
        return screenRepository.findById(id);
    }

    public List<Screen> getScreenListByParentIds(List<Long> idList) {return  screenRepository.findAllByParentIds(idList);}

    public List<Screen> getScreenList(){
        return screenRepository.findAll();
    }

    public List<Screen> getRootScreenList(){
        return screenRepository.findRootList();
    }

    public List<Screen> getScreenListWithCondition(String searchText){
        return screenRepository.findAllWithCondition(searchText);
    }


    public Page<Screen> getScreenListWithConditionAndPaging(SearchTextRequestWithPage searchTextRequestWithPage){
        return screenRepository.findAllWithConditionAndPaging(
                searchTextRequestWithPage.page(),
                searchTextRequestWithPage.size(),
                searchTextRequestWithPage.searchText()
        );
    }

    public Screen getScreen(Long id){
        return screenRepository.findById(id);
    }
}
