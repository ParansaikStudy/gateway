package com.zqksk.api.datasource.cpu;

import com.zqksk.api.domain.pc.CpuPerformance;
import com.zqksk.api.domain.pc.CpuRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CpuCoreRepository implements CpuRepository {

    private final CpuJpaRepository cpuJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public List<CpuPerformance> getCpus() {
        return cpuJpaRepository.findAll().stream()
                .map(CpuEntity::toCpuPerformance)
                .toList();
    }

    @Override
    public List<CpuPerformance> getCpusByBrand(String brand) {
        return cpuJpaRepository.findAllByBrand(brand).stream()
                .map(CpuEntity::toCpuPerformance)
                .toList();
    }
}
