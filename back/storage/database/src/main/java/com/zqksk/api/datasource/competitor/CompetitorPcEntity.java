package com.zqksk.api.datasource.competitor;

import com.zqksk.api.datasource.BaseEntity;
import com.zqksk.api.domain.common.CompetitorProgramType;
import com.zqksk.api.domain.common.ProgramType;
import com.zqksk.api.domain.competitor.CompetitorPc;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "competitor_pc")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CompetitorPcEntity  extends BaseEntity {
    @Size(max = 15)
    @Column(length = 15)
    @Comment("요양기관번호")
    private String hospitalId;

    @Size(max = 50)
    @Column(length = 50)
    @Comment("병원명")
    private String hospitalName;
    @Size(max = 20)
    @Column(length = 20)
    @Comment("PC IP")
    private String ip;
    @Size(max = 100)
    @Column(length = 100)
    @Comment("MAC 주소")
    private String macAddress;
    @Size(max = 255)
    @Column
    @Comment("PC 이름")
    private String pcName;

    @Column
    @Comment("프로그램 구분(0: 두번에, 1: 하나로, 2: OneClick, 3: OneCodi, 4: OneMessenger)")
    private int pgType;

    @Column
    @Comment("경쟁사 프로그램 ID(1:하나로 , 2:OneClick, 3:덴트웹, 4:앤드윈 , 5:제대로 , 6:아이프로 , 7:OK-Pen , 8:두번에)")
    private int competitorId;

    @Column
    @Comment("경쟁사 프로그램 설치날짜")
    private LocalDateTime competitorInstallDate;

    @Column
    @Comment("작업일시")
    private LocalDateTime workDatetime;

    @Builder
    public CompetitorPcEntity(String hospitalId, String hospitalName, String ip, String macAddress, String pcName, int pgType, int competitorId, LocalDateTime competitorInstallDate, LocalDateTime workDatetime) {
        this.hospitalId = hospitalId;
        this.hospitalName = hospitalName;
        this.ip = ip;
        this.macAddress = macAddress;
        this.pcName = pcName;
        this.pgType = pgType;
        this.competitorId = competitorId;
        this.competitorInstallDate = competitorInstallDate;
        this.workDatetime = workDatetime;
    }


    public CompetitorPcEntity update(String hospitalId, String hospitalName, String ip, String macAddress, String pcName, int pgType, int competitorId, LocalDateTime competitorInstallDate, LocalDateTime workDatetime) {
        this.hospitalId = hospitalId;
        this.hospitalName = hospitalName;
        this.ip = ip;
        this.macAddress = macAddress;
        this.pcName = pcName;
        this.pgType = pgType;
        this.competitorId = competitorId;
        this.competitorInstallDate = competitorInstallDate;
        this.workDatetime = workDatetime;
        return this;
    }



    public CompetitorPc toCompetitorPc(){
        return new CompetitorPc(
                super.getId(),
                hospitalId,
                hospitalName,
                ip,
                macAddress,
                pcName,
                ProgramType.fromCode(pgType),
                CompetitorProgramType.fromCode(competitorId),
                competitorInstallDate,
                workDatetime
        );
    }



}
