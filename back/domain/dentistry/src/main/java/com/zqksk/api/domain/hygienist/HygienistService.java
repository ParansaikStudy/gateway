package com.zqksk.api.domain.hygienist;

import org.springframework.stereotype.Service;

@Service
public class HygienistService {
    private final HygienistAppender hygienistAppender;

    public HygienistService(HygienistAppender hygienistAppender) {
        this.hygienistAppender = hygienistAppender;
    }

    public void save(CreateHygienist hygienist) {
        hygienistAppender.save(hygienist);
    }
}
