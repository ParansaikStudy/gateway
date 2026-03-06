package com.zqksk.api.datasource.user.entity;

import com.zqksk.api.datasource.BaseEntity;
import com.zqksk.api.domain.user.model.response.UserScreenAccess;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

@Entity
@Table(name = "user_screen_access")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserScreenAccessEntity extends BaseEntity {
    @Column
    @Comment("사용자 ID")
    Long userId;

    @Column
    @Comment("화면 ID")
    Long screenId;

    @Builder
    public UserScreenAccessEntity(Long userId, Long screenId) {
        this.userId = userId;
        this.screenId = screenId;
    }

    public UserScreenAccess toUserScreenAccess(){
       return new UserScreenAccess(
                super.getId(),
                userId,
                screenId
        );
    }
    
}
