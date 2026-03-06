package com.zqksk.api.datasource.hygienist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HygienistJpaRepository extends JpaRepository<HygienistEntity, Long> {
}
