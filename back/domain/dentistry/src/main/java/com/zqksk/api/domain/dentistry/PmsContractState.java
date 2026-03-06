package com.zqksk.api.domain.dentistry;

import java.util.List;

public enum PmsContractState {
    미계약(List.of(0), "미계약"),
    계약(List.of(1,2,3), "계약");

    private final List<Integer> code;
    private final String name;

    PmsContractState(List<Integer> code, String name){
        this.code = code;
        this.name = name;
    }

    public List<Integer> getCodes() {
        return this.code;
    }

    public static List<Integer> getCodesFromString(String programType) {
        try {
            return PmsContractState.valueOf(programType.toUpperCase()).getCodes();
        } catch (IllegalArgumentException e) {
            return List.of(-1);
        }
    }




}
