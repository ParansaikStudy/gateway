package com.zqksk.api.datasource.notices;

import com.zqksk.api.datasource.BaseEntity;
import com.zqksk.api.domain.common.ProgramType;
import com.zqksk.api.domain.notices.Notices;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.time.LocalDate;

@Entity
@Table(name = "notices")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NoticesEntity extends BaseEntity {
    @Size(max = 200)
    @Column(length = 200)
    @Comment("공지 제목")
    private String title;

    @Column
    @Comment("프로그램 구분(0: 두번에, 1: 하나로, 2: OneClick, 3: OneCodi, 4: OneMessenger)")
    private int pgType;

    @Lob
    @Comment("공지 내용")
    private String content;

    @Column
    @Comment("공지시작일자")
    private LocalDate noticeStartDate;

    @Column
    @Comment("공지종료일자")
    private LocalDate noticeEndDate;

     @Column
     @Comment("사용자 ID")
     private Long userId;

    @Column
    @Comment("공지 index")
    private Long index;


    @Column
    @Comment("삭제 유무")
    private String deleteYn;


     @Builder
    public NoticesEntity(String title, int pgType, String content, LocalDate noticeStartDate, LocalDate noticeEndDate, Long userId, Long index) {
        this.title = title;
        this.pgType = pgType;
        this.content = content;
        this.noticeStartDate = noticeStartDate;
        this.noticeEndDate = noticeEndDate;
        this.userId = userId;
        this.index = index;
    }

    public Notices toNotices(){
        return new Notices(
                super.getId(),
                pgType,
                ProgramType.fromCode(pgType),
                title,
                content,
                noticeStartDate,
                noticeEndDate,
                userId,
                index,
                super.getUpdatedAt()
        );
    }

    public void update(String title, int pgType, String content, LocalDate noticeStartDate, LocalDate noticeEndDate, Long userId, Long index){
        this.title = title;
        this.pgType = pgType;
        this.content = content;
        this.noticeStartDate = noticeStartDate;
        this.noticeEndDate = noticeEndDate;
        this.userId = userId;
        this.index = index;
    }
}
