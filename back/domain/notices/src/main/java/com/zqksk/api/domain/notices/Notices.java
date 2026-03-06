package com.zqksk.api.domain.notices;

import java.time.*;

public record Notices (
        Long id,
        int pgType,
        String pgTypeName,
        String title,
        String content,
        LocalDate noticeStartDate,
        LocalDate noticeEndDate,
        Long userId,
        Long index,
        LocalDateTime saveDateTime

){


}
