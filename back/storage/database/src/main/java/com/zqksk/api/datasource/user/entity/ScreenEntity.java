package com.zqksk.api.datasource.user.entity;

import com.zqksk.api.datasource.BaseEntity;
import com.zqksk.api.domain.user.model.response.Screen;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

@Entity
@Table(name = "screens")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScreenEntity extends BaseEntity {

    @Column(name = "parent_id")
    @Comment("부모 Id")
    private Long parentId;

    @Column(length = 20)
    @Comment("화면명")
    private String name;

    @Column(length = 100)
    @Comment("컴포넌트명")
    private String componentName;

    @Column(length = 500)
    @Comment("url")
    private String url;

    @Builder
    public ScreenEntity(Long parentId, String name, String componentName, String url) {
        this.parentId = parentId;
        this.name = name;
        this.componentName = componentName;
        this.url = url;
    }


    public ScreenEntity update(Long parentId, String name, String componentName, String url){
        this.parentId = parentId;
        this.name = name;
        this.componentName = componentName;
        this.url = url;
        return this;
    }

    public Screen toScreen(){
        return new Screen(
                super.getId(),
                parentId,
                name,
                componentName,
                url
        );
    }
}
