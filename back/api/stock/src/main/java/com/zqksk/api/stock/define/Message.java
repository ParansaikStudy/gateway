package com.zqksk.api.stock.define;

import lombok.Getter;

@Getter
public enum Message {

    DEFAULT_NOT_IN_TOP500("탑500에 포함된 종목만 조회 가능합니다. (없는 종목입니다.)"),
    ERROR_NOT_CODE("오류: 종목 코드가 없습니다."),
    ;

    private final String message;

    Message(String message) {
        this.message = message;
    }
}
