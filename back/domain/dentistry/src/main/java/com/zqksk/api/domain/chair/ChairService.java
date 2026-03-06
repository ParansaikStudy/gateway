package com.zqksk.api.domain.chair;

import org.springframework.stereotype.Service;

@Service
public class ChairService {
    private final ChairAppender chairAppender;

    public ChairService(ChairAppender chairAppender) {
        this.chairAppender = chairAppender;
    }

    public void createChair(CreateChair createChair) {
        chairAppender.append(createChair);
    }
}
