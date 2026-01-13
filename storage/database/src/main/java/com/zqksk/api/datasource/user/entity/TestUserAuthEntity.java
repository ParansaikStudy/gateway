package com.zqksk.api.datasource.user.entity;

import com.zqksk.api.datasource.BaseEntity;
import com.zqksk.api.domain.user.model.response.TestUserAuth;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "users_auth")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TestUserAuthEntity extends BaseEntity {
    private Long userId;

    private Long roleId;

    @Builder
    public TestUserAuthEntity(Long userId, Long roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }

    public TestUserAuth toTestUsersAuth(){
        return new TestUserAuth(
                userId,
                roleId
        );
    }
}
