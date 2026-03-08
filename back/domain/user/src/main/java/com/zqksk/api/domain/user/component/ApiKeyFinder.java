package com.zqksk.api.domain.user.component;

import com.zqksk.api.domain.user.model.response.ApiKey;
import com.zqksk.api.domain.user.repository.ApiKeyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApiKeyFinder {
    private final ApiKeyRepository apiKeyRepository;

    public ApiKey getApiKeyByKey(String key) {
        return apiKeyRepository.findByKey(key);
    }
}
