package com.zqksk.api.datasource.pc;

import com.zqksk.api.datasource.BaseEntity;
import com.zqksk.api.domain.common.ProgramType;
import com.zqksk.api.domain.common.WorkType;
import com.zqksk.api.domain.pc.Pc;
import com.zqksk.api.domain.pc.PcResponse;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@Entity
@Table(name = "pc")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PcEntity extends BaseEntity {

    @Column
    @Comment("요양기관번호")
    private String hospitalId;

    @Column
    @Comment("병원명")
    private String hospitalName;

    @Column
    @Comment("PC IP")
    private String ip;

    @Column
    @Comment("MAC 주소")
    private String macAddress;

    @Column
    @Comment("PC 이름")
    private String pcName;

    @Column
    @Comment("CPU")
    private String cpu;

    @Column
    @Comment("메모리")
    private String memory;

    @Column
    @Comment("GPU")
    private String gpu;

    @Column
    @Comment("OS")
    private String os;

    @Column
    @Comment("클라이언트/서버구분(0: Server, 1: Client)")
    private int workType;

    @Column
    @Comment("프로그램 구분(0: 두번에, 1: 하나로, 2: OneClick, 3: OneCodi, 4: OneMessenger, 5: OneServer, 6: One2  7" +
            ": One3 ,8: V-ceph, 9: OneClickM, 10: OneDeskM, 11: OnePhoto, 12: OneChartScanM, 13: OneCodiM , 14: DBMigration)")
    private int pgType;

    @Column
    @Comment("버전(15.0.0.1)")
    private String version;

    @Column
    @Comment("성능 사양 점수")
    private Integer perfSpecScore;

    @Column
    @Comment("모니터 해상도")
    private String monResol;

    @Column
    @Comment("최초작업일자")
    private LocalDateTime firstDatetime;

    @Column
    @Comment("최종작업일자")
    private LocalDateTime lastDatetime;

    @Builder
    public PcEntity(String hospitalId, String hospitalName, String ip, String macAddress, String pcName, String cpu, String memory, String gpu, String os, int workType, int pgType, String version, int perfSpecScore, String monResol, LocalDateTime firstDatetime, LocalDateTime lastDatetime) {
        this.hospitalId = hospitalId;
        this.hospitalName = hospitalName;
        this.ip = ip;
        this.macAddress = macAddress;
        this.pcName = pcName;
        this.cpu = cpu;
        this.memory = memory;
        this.gpu = gpu;
        this.os = os;
        this.workType = workType;
        this.pgType = pgType;
        this.version = version;
        this.perfSpecScore = perfSpecScore;
        this.monResol = monResol;
        this.firstDatetime = firstDatetime;
        this.lastDatetime = lastDatetime;
    }

    public PcEntity update(String hospitalId, String hospitalName, String ip, String pcName, String cpu, String memory, String gpu, String os, int workType, int pgType, String version, String monResol, LocalDateTime lastDatetime) {
        this.hospitalId = hospitalId;
        this.hospitalName = hospitalName;
        this.ip = ip;
        this.pcName = pcName;
        this.cpu = cpu;
        this.memory = memory;
        this.gpu = gpu;
        this.os = os;
        this.workType = workType;
        this.pgType = pgType;
        this.version = version;
        this.monResol = monResol;
        this.lastDatetime = lastDatetime;
        return this;
    }

    public PcEntity updateScore(int perfSpecScore) {
        this.perfSpecScore = perfSpecScore;
        return this;
    }

    public Pc toPc() {
        return new Pc(
                super.getId(),
                hospitalId,
                hospitalName,
                ip,
                macAddress,
                pcName,
                cpu,
                memory,
                gpu,
                os,
                WorkType.fromCode(workType),
                ProgramType.fromCode(pgType),
                version,
                perfSpecScore != null ? perfSpecScore : 0,
                monResol,
                firstDatetime,
                lastDatetime
        );
    }

    public PcResponse toPcResponse(){
        return new PcResponse(
                super.getId(),
                hospitalId,
                hospitalName,
                ip,
                macAddress,
                pcName,
                cpu,
                memory,
                gpu,
                os,
                WorkType.fromCode(workType),
                ProgramType.fromCode(pgType),
                version,
                perfSpecScore != null ? perfSpecScore : 0,
                monResol,
                null,
                firstDatetime,
                lastDatetime
        );

    }
}
