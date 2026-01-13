package com.zqksk.api.domain.notices;

import java.time.*;

public record UpdateNotices(
        Long id,
        int pgType,
        String title,
        String content,
        LocalDate noticeStartDate,
        LocalDate noticeEndDate,
        Long userId,
        Long index
) {


    public static UpdateNotices create(Long id, int pgType, String title, String content, LocalDate noticeStartDate, LocalDate noticeEndDate, Long userId, Long index ,Long lastIndex){


        Long nowIndex = (noticeStartDate!=null && noticeEndDate!=null && assignIndex(noticeStartDate, noticeEndDate)) ? (index != null ? index : lastIndex + 1): null;


        return new UpdateNotices(id, pgType, title, content, noticeStartDate, noticeEndDate,userId, nowIndex);
    }
    private static boolean assignIndex(LocalDate noticeStartDate, LocalDate noticeEndDate){
        Instant now = Instant.now();

        LocalDate today = now.atZone(ZoneId.of("UTC")).toLocalDate();

        return (today.isEqual(noticeStartDate) || today.isAfter(noticeStartDate)) &&
                (today.isEqual(noticeEndDate) || today.isBefore(noticeEndDate));

    }
}
