package com.zqksk.api.domain.log;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class LogAppender {

    private final LogRepository logRepository;

    public Log append(NewLog log) {
        return logRepository.save(log);
    }

    public Long deleteByWorkDatetimeAtBefore(LocalDateTime localDateTime) {
        return logRepository.deleteByWorkDatetimeAtBefore(localDateTime);
    }
}
