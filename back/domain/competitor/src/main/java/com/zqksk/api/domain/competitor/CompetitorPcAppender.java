package com.zqksk.api.domain.competitor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CompetitorPcAppender {

    private final CompetitorPcRepository competitorPcRepository;

    public CompetitorPc append(final NewCompetitorPc newCompetitorPc) {
        return competitorPcRepository.save(newCompetitorPc);
    }


}
