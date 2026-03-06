package com.zqksk.api.domain.pc;


public enum Brand {

    INTEL("INTEL"),
    AMD("AMD"),
    APPLE("APPLE"),
    ARM("ARM"),
    QUALCOMM("QUALCOMM"),
    MEDIATEK("MEDIATEK"),
    SAMSUNG("SAMSUNG"),
    VIA("VIA"),
    UNKNOWN("UNKNOWN");

    private final String keyword;

    Brand(String keyword) {
        this.keyword = keyword;
    }

    public String getKeyword() {
        return keyword;
    }

    public static Brand from(String cpuName) {
        if (cpuName == null) {
            return UNKNOWN;
        }

        String upper = cpuName.toUpperCase();
        Brand detected = null;

        for (Brand brand : values()) {
            if (brand == UNKNOWN) continue;

            if (upper.contains(brand.keyword)) {
                // 이미 다른 브랜드가 감지된 경우 → 다중 매칭 → 규칙상 UNKNOWN
                if (detected != null) {
                    return UNKNOWN;
                }
                detected = brand;
            }
        }

        return detected != null ? detected : UNKNOWN;
    }
}
