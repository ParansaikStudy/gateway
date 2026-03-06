package com.zqksk.api.datasource.log;

import com.zqksk.api.datasource.BaseEntity;
import com.zqksk.api.domain.common.EventType;
import com.zqksk.api.domain.log.Log;
import com.zqksk.api.domain.common.ProgramType;
import com.zqksk.api.domain.common.WorkType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@Entity
@Table(name = "log")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LogEntity extends BaseEntity {

    @Column
    @Comment("요양기관번호")
    private String hospitalId;

    @Column
    @Comment("병원명")
    private String hospitalName;

    @Column
    @Comment("작업일시")
    private LocalDateTime workDatetime;

    @Column
    @Comment("사용자ID")
    private String userId;

    @Column
    @Comment("환자 ID")
    private String patientUid;

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
    @Comment("이벤트 타입(0: Info, 1: Debug, 2: Warning, 3: Error, 4: Fatal)")
    @ColumnDefault("0")
    private int eventType;

    @Column
    @Comment("대메뉴")
    private String rootMenu;

    @Column
    @Comment("화면ID")
    private String viewId;

    @Column
    @Comment("화면명")
    private String viewName;

    @Column
    @Comment("오류 코드")
    private String errorCode;

    @Column
    @Comment("Log 정보")
    private String log;

    @Column
    @Comment("Log 상세정보")
    private String logDetail;

    @Column
    @Comment("Log 기타정보")
    private String etc;

    @Column
    @Comment("클라이언트/서버 구분(0: Server, 1: Client)")
    private int workType;

    @Column
    @Comment("버전(15.0.0.1)")
    private String version;

    @Column
    @Comment("프로그램 구분(0: 두번에, 1: 하나로, 2: OneClick, 3: OneCodi, 4: OneMessenger)")
    private int programType;

    @Builder
    public LogEntity(String hospitalId, String hospitalName, LocalDateTime workDatetime, String userId, String patientUid, String ip, String macAddress, String pcName, int eventType, String rootMenu, String viewId, String viewName, String errorCode,String log, String logDetail, String etc, int workType, String version, int programType) {
        this.hospitalId = hospitalId;
        this.hospitalName = hospitalName;
        this.workDatetime = workDatetime;
        this.userId = userId;
        this.patientUid = patientUid;
        this.ip = ip;
        this.macAddress = macAddress;
        this.pcName = pcName;
        this.eventType = eventType;
        this.rootMenu = rootMenu;
        this.viewId = viewId;
        this.viewName = viewName;
        this.errorCode = errorCode;
        this.log = log;
        this.logDetail = logDetail;
        this.etc = etc;
        this.workType = workType;
        this.version = version;
        this.programType = programType;
    }

    public Log toLog() {
        return new Log(
                super.getId(),
                hospitalId,
                hospitalName,
                workDatetime,
                userId,
                patientUid,
                ip,
                macAddress,
                pcName,
                EventType.fromCode(eventType),
                rootMenu,
                viewId,
                viewName,
                errorCode,
                log,
                logDetail,
                etc,
                WorkType.fromCode(workType),
                version,
                ProgramType.fromCode(programType)
        );
    }
}
