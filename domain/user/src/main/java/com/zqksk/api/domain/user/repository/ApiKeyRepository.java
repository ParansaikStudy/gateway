package com.zqksk.api.domain.user.repository;

import com.zqksk.api.domain.user.model.response.ApiKey;

public interface ApiKeyRepository {
    ApiKey findByKey(String key);
}
