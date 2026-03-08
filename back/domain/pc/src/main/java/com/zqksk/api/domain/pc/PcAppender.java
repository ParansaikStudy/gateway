package com.zqksk.api.domain.pc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PcAppender {

    private final PcRepository pcRepository;

    public Pc append(final NewPc newPc) {
        return pcRepository.save(newPc);
    }

    public Pc appendScore(final NewPcScore newPcScore) {
        return pcRepository.updateScore(newPcScore.id(), newPcScore.totalScore());
    }

    public long deleteByWorkDatetimeAtBefore(LocalDateTime localDateTime) {
        return pcRepository.deleteByWorkDatetimeAtBefore(localDateTime);

    }



}
