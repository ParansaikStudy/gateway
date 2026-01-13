package com.zqksk.api.domain.user.model;

import lombok.Getter;

@Getter
public enum AuthorityType {
    SUPER_ADMIN("슈퍼 관리자"),
    ONECLICK_SERVICE_MANAGER("원클릭 관리자"),
    ADMIN("admin"),
    ENGINEER("engineer"),
    STANDARD_USER("standard user"),
    GUEST("guest"),
    PM("프로젝트매니저");

    private final String name;

    AuthorityType(String name) {
        this.name = name;
    }

    public static AuthorityType fromName(String name) {
        for (AuthorityType type : AuthorityType.values()) {
            if (type.name.equalsIgnoreCase(name)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown name: " + name);
    }
}
