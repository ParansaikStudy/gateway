package com.zqksk.api.domain.chair;

import org.springframework.stereotype.Component;

@Component
public class ChairAppender {
    private final ChairRepository chairRepository;

    public ChairAppender(ChairRepository chairRepository) {
        this.chairRepository = chairRepository;
    }

    public void append(CreateChair createChair) {
        chairRepository.save(createChair.toChair());
    }
}
