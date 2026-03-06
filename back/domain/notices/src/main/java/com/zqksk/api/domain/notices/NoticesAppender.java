package com.zqksk.api.domain.notices;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NoticesAppender {

    private final NoticeRepository noticeRepository;


    public Notices append(final NewNotices newNotices){
        return  noticeRepository.save(newNotices);
    }

    public Notices update(final NewNotices newNotices){return noticeRepository.update(newNotices);}

    public Long clearIndexForExpiredNotices(){return noticeRepository.clearIndexForExpiredNotices();}

    public Long delete(final Long id){return noticeRepository.deleteById(id);}
    public Long deleteListById(final List<Long> idList){return noticeRepository.deleteAllById(idList);}

    public void changeIndexById(Long id, Long index){
        noticeRepository.changeIndexById(id, index);
    }

}
