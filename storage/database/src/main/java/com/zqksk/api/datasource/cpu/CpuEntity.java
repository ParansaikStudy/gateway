package com.zqksk.api.datasource.cpu;

import com.zqksk.api.datasource.BaseEntity;
import com.zqksk.api.domain.common.ProgramType;
import com.zqksk.api.domain.common.WorkType;
import com.zqksk.api.domain.pc.CpuPerformance;
import com.zqksk.api.domain.pc.Pc;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@Entity
@Table(name = "cpus")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CpuEntity extends BaseEntity {

    @Column
    @Comment("CPU 브랜드")
    private String brand;

    @Column
    @Comment("CPU 명")
    private String name;

    @Column
    @Comment("PC Score")
    private int score;

    @Builder
    public CpuEntity(String brand, String name, int score) {
        this.brand = brand;
        this.name = name;
        this.score = score;
    }

    public CpuPerformance toCpuPerformance() {
        return new CpuPerformance(brand, name, score);
    }
}
