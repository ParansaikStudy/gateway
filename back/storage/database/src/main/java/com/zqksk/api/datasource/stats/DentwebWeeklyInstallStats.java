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
@Table(name = "dentweb_weekly_install_stats")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DentwebWeeklyInstallStats extends BaseEntity {

    @Column
    @Comment("프로그램 구분(0: 두번에, 1: 하나로, 2: OneClick, 3: OneCodi, 4: OneMessenger, 5: OneServer, 6: One2  7" +
            ": One3 ,8: V-ceph, 9: OneClickM, 10: OneDeskM, 11: OnePhoto, 12: OneChartScanM, 13: OneCodiM , 14: DBMigration)")
    private int programType;

    @Column
    @Comment("주 시작일")
    private LocalDate weekStartDate;

    @Column
    @Comment("주 종료일")
    private LocalDate weekEndDate;

    @Column
    @Comment("설치 수")
    private int installCount;
}
