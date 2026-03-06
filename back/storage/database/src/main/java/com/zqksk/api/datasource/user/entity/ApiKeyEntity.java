package com.zqksk.api.datasource.user.entity;

import com.zqksk.api.datasource.BaseEntity;
import com.zqksk.api.domain.user.model.response.ApiKey;
import com.zqksk.api.domain.user.model.response.UserAuth;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@Table(name = "api_keys")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiKeyEntity extends BaseEntity {
    @Size(max = 100)
    @NotNull
    @Column(name = "key_name", nullable = false, length = 100)
    private String keyName;

    @Size(max = 255)
    @NotNull
    @Column(name = "`key`", nullable = false)
    private String key;

    @Column(name = "user_id")
    private Long userId;

    public ApiKey toApiKey() {
        return ApiKey.builder()
                .id(super.getId())
                .keyName(keyName)
                .key(key)
                .userId(userId)
                .build();
    }
}