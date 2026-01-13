package com.zqksk.api.domain.user.model;

import java.util.Arrays;

public enum Position {
    CHAIRMAN("001", "회장"),
    PRESIDENT("010", "사장"),
    ADVISOR("015", "고문"),
    VICE_CHAIRMAN("016", "부회장"),
    VICE_PRESIDENT("020", "부사장"),
    EXECUTIVE_DIRECTOR("030", "전무이사"),
    MANAGING_DIRECTOR("040", "상무이사"),
    MANAGING_DIRECTOR_HONORARY("041", "상무이사(역)"),
    CONSULTANT("045", "자문"),
    EXPERT_ADVISOR("046", "전문위원"),
    DIRECTOR("050", "이사"),
    STANDING_AUDITOR("055", "상근감사"),
    CHIEF_SECTION_HEAD("100", "수석부장"),
    CHIEF_FOREMAN("120", "수석직장"),
    CHIEF_RESEARCHER("130", "수석연구원"),
    MASTER_CRAFTSMAN("140", "명장"),
    SECTION_HEAD("200", "부장"),
    SENIOR_RESEARCHER("210", "책임연구원"),
    FOREMAN("220", "직장"),
    CHIEF_ENGINEER("230", "수석기장"),
    DEPUTY_SECTION_HEAD("300", "차장"),
    ADVANCED_RESEARCHER("310", "선임연구원"),
    CHIEF_LEADER("320", "수석반장"),
    VISITING_RESEARCHER("330", "초빙연구원"),
    ENGINEER("340", "기장"),
    MANAGER("400", "과장"),
    ASSISTANT_RESEARCHER("410", "주임연구원"),
    LEADER("420", "반장"),
    SENIOR_TECHNICIAN("430", "수석기사"),
    ASSISTANT_MANAGER("500", "대리"),
    RESEARCHER("510", "연구원"),
    TECHNICIAN("530", "기사"),
    SUPERVISOR("550", "주임"),
    CENTER_DIRECTOR("060", "원장"),
    STAFF("600", "사원"),
    RESEARCH_ASSISTANT("620", "연구보조원"),
    DISPATCHED_EMPLOYEE("640", "파견사원"),
    INTERN("650", "실습생"),
    DIRECTOR_ENG("960", "Director"),
    SENIOR_CHIEF_TS("961", "선임수석TS"),
    CHIEF_TS("962", "수석TS"),
    DEPUTY_CHIEF_TS("963", "차석TS"),
    SENIOR_TS("964", "책임TS"),
    ADVANCED_TS("965", "선임TS"),
    ASSOCIATE_TS("966", "전임TS"),
    JUNIOR_TS("967", "주임TS"),
    MANAGER_ENG("968", "Manager"),
    MARKETING_DIRECTOR("969", "Marketing Director"),
    ASSISTANT_MANAGER_ENG("970", "Asst Manager"),
    ASSOCIATE("971", "Associate"),
    TEAM_LEADER("972", "Team Leader"),
    SUPERVISOR_ENG("973", "Supervisor"),
    SENIOR_MANAGER("974", "Senior Manager");

    private final String code;
    private final String name;

    Position(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static Position findByCode(String code) {
        return Arrays.stream(values())
                .filter(position -> position.code.equals(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid position code: " + code));
    }

    public static Position findByName(String name) {
        return Arrays.stream(values())
                .filter(position -> position.name.equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid position name: " + name));
    }
}
