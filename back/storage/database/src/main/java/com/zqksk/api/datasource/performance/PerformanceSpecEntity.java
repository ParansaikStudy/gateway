package com.zqksk.api.datasource.performance;

import com.zqksk.api.datasource.BaseEntity;
import com.zqksk.api.domain.pc.PerformanceSpec;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Entity
@Table(name = "performance_spec")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PerformanceSpecEntity extends BaseEntity {
    @NotNull
    @Column(name = "software_id", nullable = false)
    private int softwareId;

    @ColumnDefault("0")
    @Column(name = "work_type")
    private int workType;

    @NotNull
    @Column(name = "min_score", nullable = false)
    private int minScore;

    @NotNull
    @Column(name = "rec_score", nullable = false)
    private int recScore;

    @NotNull
    @Column(name = "max_score", nullable = false)
    private int maxScore;

    @Builder
    public PerformanceSpecEntity(int softwareId, int workType, int minScore, int recScore, int maxScore) {
        this.softwareId = softwareId;
        this.workType = workType;
        this.minScore = minScore;
        this.recScore = recScore;
        this.maxScore = maxScore;
    }

    public PerformanceSpec toPerformanceSpec() {
        return new PerformanceSpec(softwareId, workType, minScore, recScore, maxScore);
    }
}