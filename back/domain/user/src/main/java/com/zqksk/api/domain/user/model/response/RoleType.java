package com.zqksk.api.domain.user.model.response;

public enum RoleType {
    ADMIN("관리자"),
    ONECLICK_SERVICE_MANAGER("원클릭서비스매니저"),
    STANDARD_USER("기본유저"),
    GUEST("게스트"),
    ENGINEER("개발자"),
    PM("프로젝트매니저");

    private final String name;

    RoleType(String name) {this.name = name;}

    public static RoleType from(String value) {return valueOf(value);}

}
