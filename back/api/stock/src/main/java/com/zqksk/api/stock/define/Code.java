package com.zqksk.api.stock.define;

import lombok.Getter;

@Getter
public enum Code {

    NVIDIA("엔비디아", "nvidia", "nvda"),
    APPLE("애플", "apple", "aapl"),
    ;

    private final String krName;
    private final String enName;
    private final String abName;

    Code(String krName, String enName, String abName) {
        this.krName = krName;
        this.enName = enName;
        this.abName = abName;
    }

    public static Code fromValue(String key) {
        if (key == null) {
            return null;
        }
        for (Code type : values()) {
            if (type.krName.equalsIgnoreCase(key)) {
                return type;
            }
        }
        return null;
    }
}
