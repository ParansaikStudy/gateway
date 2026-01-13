package com.zqksk.mail.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.task.ThreadPoolTaskExecutorCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.ThreadPoolExecutor;

@EnableAsync
@Configuration(proxyBeanMethods = false)
public class MailAsyncConfig {
    @Bean
    @ConditionalOnProperty(
            prefix = "spring.threads.virtual",
            name = "enabled",
            havingValue = "false",
            matchIfMissing = true
    )
    ThreadPoolTaskExecutorCustomizer threadPoolTaskExecutorCustomizer() {
        return threadPoolTaskExecutor -> {
            int corePoolSize = Runtime.getRuntime().availableProcessors();
            int maxPoolSize = corePoolSize * 2;
            int queueCapacity = maxPoolSize * 8;
            threadPoolTaskExecutor.setCorePoolSize(corePoolSize);
            threadPoolTaskExecutor.setMaxPoolSize(maxPoolSize);
            threadPoolTaskExecutor.setQueueCapacity(queueCapacity);
            threadPoolTaskExecutor.setThreadNamePrefix("MAIL-ASYNC-TASK");
            threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
            threadPoolTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
            threadPoolTaskExecutor.setAwaitTerminationSeconds(30);
            threadPoolTaskExecutor.setStrictEarlyShutdown(false);
        };
    }
}
