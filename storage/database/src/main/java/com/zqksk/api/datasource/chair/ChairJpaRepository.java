package com.zqksk.api.datasource.chair;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChairJpaRepository extends JpaRepository<ChairEntity, Long> {
}
