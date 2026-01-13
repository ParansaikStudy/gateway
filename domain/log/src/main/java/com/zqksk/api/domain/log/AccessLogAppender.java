package com.zqksk.api.domain.log;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class AccessLogAppender {

    private final AccessLogRepository accessLogRepository;

    public AccessLog append(NewAccessLog newAccessLog) {
        return accessLogRepository.save(newAccessLog);
    }

    public Long deleteByWorkDatetimeAtBefore(LocalDateTime localDateTime) {
        return accessLogRepository.deleteByWorkDatetimeAtBefore(localDateTime);
    }
}
