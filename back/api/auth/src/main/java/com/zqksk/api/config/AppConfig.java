package com.zqksk.api.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.zqksk.api.model.CacheType;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class AppConfig {

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(
                Arrays.stream(CacheType.values())
                        .map(cache -> new CaffeineCache(cache.getName(),
                                Caffeine.newBuilder()
                                        .expireAfterWrite(cache.getExpireAfterWrite(), TimeUnit.SECONDS)
                                        .maximumSize(cache.getMaximumSize())
                                        .build()))
                        .toList()
        );

        return cacheManager;
    }
}
