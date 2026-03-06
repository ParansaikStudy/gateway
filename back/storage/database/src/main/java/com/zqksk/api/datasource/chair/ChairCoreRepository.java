package com.zqksk.api.datasource.chair;

import com.zqksk.api.domain.chair.Chair;
import com.zqksk.api.domain.chair.ChairRepository;
import org.springframework.stereotype.Repository;

@Repository
public class ChairCoreRepository implements ChairRepository {
    private final ChairJpaRepository chairJpaRepository;

    public ChairCoreRepository(ChairJpaRepository chairJpaRepository) {
        this.chairJpaRepository = chairJpaRepository;
    }

    @Override
    public void save(Chair chair) {
        chairJpaRepository.save(new ChairEntity(
                chair.chairId(),
                chair.manufacturer(),
                chair.modelName())
        );
    }
}
