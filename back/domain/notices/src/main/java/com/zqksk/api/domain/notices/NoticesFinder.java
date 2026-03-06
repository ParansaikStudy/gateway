package com.zqksk.api.domain.notices;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NoticesFinder {

    private final NoticeRepository noticeRepository;

    public List<Notices> getList(){
        return noticeRepository.findAll();
    }

    public Page<Notices> getListWithConditionAndPaging(int page, int size, String searchText, Integer pgType){
        return noticeRepository.findAllWithConditionAndPaging(page, size, searchText, pgType);

    }

    public List<Notices> getListWithPgType(Integer pgType){return noticeRepository.findAllByPgType(pgType);}

    public Long findLastIndex(){
        return noticeRepository.findLastIndex();
    }

    public Notices getNotices(Long id){
        return noticeRepository.findById(id);
    }

    public List<Notices> findAllActiveNoticesWithoutIndex(){return noticeRepository.findAllActiveNoticesWithoutIndex();}
}
