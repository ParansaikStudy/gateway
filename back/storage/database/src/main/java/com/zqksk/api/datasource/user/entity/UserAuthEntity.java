package com.zqksk.api.datasource.user.entity;

import com.zqksk.api.datasource.BaseEntity;
import com.zqksk.api.domain.user.model.response.UserAuth;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "users_auth")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserAuthEntity extends BaseEntity {
    private Long roleId;
    private Long userId;

    @Builder
    public UserAuthEntity(Long roleId, Long userId) {
        this.roleId = roleId;
        this.userId = userId;
    }

    public UserAuth toUserAuth() {
        return new UserAuth(this.getId(), roleId, userId);
    }

    public UserAuthEntity updateRoleId(Long roleId){
        this.roleId = roleId;
        return this;
    }
}
