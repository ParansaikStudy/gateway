package com.zqksk.api.domain.hygienist;

import org.springframework.stereotype.Component;

@Component
public class HygienistAppender {
    private final HygienistRepository hygienistRepository;

    public HygienistAppender(HygienistRepository hygienistRepository) {
        this.hygienistRepository = hygienistRepository;
    }

    public void save(CreateHygienist hygienist) {
        hygienistRepository.save(hygienist.toHygienist());
    }
}
