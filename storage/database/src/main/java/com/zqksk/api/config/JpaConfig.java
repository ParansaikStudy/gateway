package com.zqksk.api.config;

import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EntityScan(basePackages = "com.zqksk.api.datasource")
@EnableJpaRepositories(basePackages = "com.zqksk.api.datasource")
public class JpaConfig {
}
