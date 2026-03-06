package com.zqksk.api.datasource.user.entity;

import com.zqksk.api.datasource.BaseEntity;
import com.zqksk.api.domain.user.model.response.RoleScreenAccess;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

@Entity
@Table(name = "role_screen_access")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoleScreenAccessEntity extends BaseEntity {
    @Column
    @Comment("역할 ID")
    private Long roleId;

    @Column
    @Comment("화면 ID")
    private Long screenId;

    @Builder
    public RoleScreenAccessEntity(Long roleId, Long screenId) {
        this.roleId = roleId;
        this.screenId = screenId;
    }

    public RoleScreenAccess toRoleScreenAccess(){
        return  new RoleScreenAccess(
                super.getId(),
                roleId,
                screenId
        );
    }

}
