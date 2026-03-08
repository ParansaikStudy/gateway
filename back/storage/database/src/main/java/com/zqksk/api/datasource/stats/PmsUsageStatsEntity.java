package com.zqksk.api.datasource.stats;

import com.zqksk.api.datasource.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDate;

@Getter
@Entity
@Table(name = "pms_usage_stats")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PmsUsageStatsEntity extends BaseEntity {

    @Column
    @Comment("프로그램 구분(0: 두번에, 1: 하나로, 2: OneClick, 3: OneCodi, 4: OneMessenger)")
    private int programType;

    @Column
    @Comment("사용 수")
    private int usageCount;

    @Column
    @Comment("통계 날짜")
    private LocalDate statDate;
}
