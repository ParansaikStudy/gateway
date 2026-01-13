package com.zqksk.api.domain.notices;

import org.springframework.data.domain.Page;

import java.util.List;

public interface NoticesService {

    default Notices save(NoticesRequest noticesRequest){throw new UnsupportedOperationException("Not implemented");}

    default Long clearIndexForExpiredNotices(){throw new UnsupportedOperationException("Not implemented");}

    default Long assignIndexForActiveNotices(){throw new UnsupportedOperationException("Not implemented");}

    default Notices update(NoticesRequest noticesRequest){throw new UnsupportedOperationException("Not implemented");}

    default Long delete(Long id){throw new UnsupportedOperationException("Not implemented");}

    default Long deleteNoticesList(List<Long> idList){throw new UnsupportedOperationException("Not implemented");}

    default void swapIndex(NoticesIndex firstNotice, NoticesIndex secondNotice){throw new UnsupportedOperationException("Not implemented");}
    default List<Notices> getNoticesList(){throw new UnsupportedOperationException("Not implemented");}

    default Page<Notices> getNoticesListWithConditionAndPaging(SearchNoticesRequest searchNoticesRequest){throw new UnsupportedOperationException("Not implemented");}

    default List<Notices> getNoticesListWithPgType(Integer pgType){throw new UnsupportedOperationException("Not implemented");}

    default Notices getNotices(Long id){throw new UnsupportedOperationException("Not implemented");}


}
