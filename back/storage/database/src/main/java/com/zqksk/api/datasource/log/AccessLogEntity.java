package com.zqksk.api.datasource.log;

import com.zqksk.api.datasource.BaseEntity;
import com.zqksk.api.domain.common.*;
import com.zqksk.api.domain.log.AccessLog;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@Entity
@Table(name = "access_log")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccessLogEntity extends BaseEntity {
    @Size(max = 15)
    @Column(name = "hospital_id", length = 15)
    @Comment("요양기관번호")
    private String hospitalId;

    @Size(max = 50)
    @NotNull
    @Column(name = "hospital_name", nullable = false, length = 50)
    @Comment("병원명")
    private String hospitalName;

    @NotNull
    @ColumnDefault("current_timestamp(6)")
    @Column(name = "work_datetime", nullable = false)
    @Comment("작업일시")
    private LocalDateTime workDatetime;

    @Size(max = 255)
    @Column(name = "user_id")
    @Comment("사용자ID")
    private String userId;

    @Size(max = 20)
    @Column(name = "ip", length = 20)
    @Comment("PC IP")
    private String ip;

    @Size(max = 100)
    @Column(name = "mac_address", length = 100)
    @Comment("MAC 주소")
    private String macAddress;

    @Size(max = 255)
    @Column(name = "pc_name")
    @Comment("PC 이름")
    private String pcName;

    @NotNull
    @Column(name = "event_type", nullable = false)
    @Comment("이벤트 타입(0: 기본, 11: 전환 속도, 12: 동기화)")
    @ColumnDefault("0")
    private int eventType;

    @Size(max = 38)
    @Column(name = "root_menu", length = 38)
    @Comment("대메뉴")
    private String rootMenu;

    @Size(max = 38)
    @Column(name = "view_id", length = 38)
    @Comment("화면ID")
    private String viewId;

    @Size(max = 255)
    @Column(name = "view_name")
    @Comment("화면명")
    private String viewName;

    @Size(max = 255)
    @Column(name = "function_name", nullable = false)
    @Comment("기능")
    private String functionName;

    @Size(max = 255)
    @Column(name = "element_name")
    @Comment("요소")
    private String elementName;

    @Column(name = "action")
    @Comment("동작")
    private int action;

    @Size(max = 255)
    @Column(name = "description")
    @Comment("설명")
    private String description;

    @Size(max = 20)
    @Column(name = "loading_speed")
    @Comment("로딩속도(s)")
    private String loadingSpeed;

    @Size(max = 255)
    @Column(name = "remark")
    @Comment("비고")
    private String remark;

    @Size(max = 20)
    @Column(name = "version", length = 20)
    @Comment("버전")
    private String version;

    @NotNull
    @Column(name = "program_type", nullable = false)
    @Comment("프로그램 구분(0: 두번에, 1: 하나로, 2: OneClick, 3: OneCodi, 4: OneMessenger)")
    private int programType;

    @Builder
    public AccessLogEntity(String hospitalId, String hospitalName, LocalDateTime workDatetime, String userId, String ip, String macAddress, String pcName, int eventType, String rootMenu, String viewId, String viewName, String functionName, String elementName, int action, String description, String loadingSpeed, String remark, String version, int programType) {
        this.hospitalId = hospitalId;
        this.hospitalName = hospitalName;
        this.workDatetime = workDatetime;
        this.userId = userId;
        this.ip = ip;
        this.macAddress = macAddress;
        this.pcName = pcName;
        this.eventType = eventType;
        this.rootMenu = rootMenu;
        this.viewId = viewId;
        this.viewName = viewName;
        this.functionName = functionName;
        this.elementName = elementName;
        this.action = action;
        this.description = description;
        this.loadingSpeed = loadingSpeed;
        this.remark = remark;
        this.version = version;
        this.programType = programType;
    }

    public AccessLog toAccessLog() {
        return new AccessLog(
            getId(),
            workDatetime,
                ProgramType.fromCode(programType),
            version,
            rootMenu,
            viewId,
            viewName,
            functionName,
            elementName,
            loadingSpeed,
            description,
            Action.fromCode(action),
            hospitalName,
            hospitalId,
            pcName,
            ip,
            macAddress,
            userId,
            AccessLogEventType.fromCode(eventType),
            remark
        );
    }
}