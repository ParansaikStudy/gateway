package com.zqksk.api.datasource.user.repository;

import com.zqksk.api.domain.user.model.response.ApiKey;
import com.zqksk.api.domain.user.repository.ApiKeyRepository;
import com.zqksk.api.exception.AuthenticationErrorType;
import com.zqksk.api.support.exception.CoreException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ApiKeysCoreRepository implements ApiKeyRepository {
    private final ApiKeysJpaRepository apiKeysJpaRepository;

    @Override
    public ApiKey findByKey(String key) {
        return apiKeysJpaRepository.findByKey(key).orElseThrow(
                () -> new CoreException(AuthenticationErrorType.INVALID_KEY)
        ).toApiKey();
    }
}
