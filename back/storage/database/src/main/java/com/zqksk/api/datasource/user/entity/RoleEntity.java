package com.zqksk.api.datasource.user.entity;

import com.zqksk.api.datasource.BaseEntity;
import com.zqksk.api.domain.user.model.response.Role;
import com.zqksk.api.domain.user.model.response.RoleType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Table(name = "roles")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoleEntity extends BaseEntity {

    @Column
    @Comment("역할 구분 = 1:전체관리자, 2:원클리서버담당자, 3:개발자, 4:게스트")
    @Enumerated(EnumType.STRING)
    private RoleType type;

    @Column(length = 100)
    @Comment("역할명")
    private String name;

    @Builder
    public RoleEntity(RoleType type, String name) {
        this.type = type;
        this.name = name;
    }

    public Role toRole(){
        return new Role(
                super.getId(),
                type == null ? null :type.name(),
                name
        );
    }

}
