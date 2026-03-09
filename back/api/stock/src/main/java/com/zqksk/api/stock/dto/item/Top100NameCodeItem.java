package com.zqksk.api.stock.dto.item;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 탑100 종목명-코드 매핑 (00시 배치에서 생성한 파일에서 종목명 검색용).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Top100NameCodeItem {
    /** 종목명 (공백 제거 후 매칭에 사용) */
    private String name;
    /** 국내 6자리 종목코드 */
    private String code;
}
