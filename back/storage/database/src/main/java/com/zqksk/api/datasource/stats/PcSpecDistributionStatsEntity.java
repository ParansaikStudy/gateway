package com.zqksk.api.datasource.stats;

import com.zqksk.api.datasource.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Entity
@Table(name = "pc_spec_distribution_stats")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PcSpecDistributionStatsEntity extends BaseEntity {

    @Column
    @Comment("프로그램 구분(0: 두번에, 1: 하나로, 2: OneClick, 3: OneCodi, 4: OneMessenger, 5: OneServer, 6: One2  7" +
            ": One3 ,8: V-ceph, 9: OneClickM, 10: OneDeskM, 11: OnePhoto, 12: OneChartScanM, 13: OneCodiM , 14: DBMigration)")
    private int programType;

    @Column
    @Comment("클라이언트/서버 구분(0: Server, 1: Client)")
    private int workType;

    @Column
    @Comment("사양 구분(0: 미지정, 1: 저사양, 2: 최소사양, 3: 권장사양, 4: 고사양)")
    private int specType;

    @Column
    @Comment("PC 수")
    private int pcCount;

    @Column
    @Comment("전체 PC 수")
    private int totalPcCount;

    @Column
    @Comment("비율")
    private BigDecimal rate;

    @Column
    @Comment("통계 날짜")
    private LocalDate statDate;
}
