package com.zqksk.api.datasource.log;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LogJpaRepository extends JpaRepository<LogEntity, Long> {
}
