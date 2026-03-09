package com.zqksk.api.stock.dto.kakao;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

/**
 * 카카오 i 오픈빌더 스킬 응답 (version 2.0, simpleText).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class KakaoSkillResponse {

    private String version;
    private SkillTemplate template;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class SkillTemplate {
        private List<OutputItem> outputs;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class OutputItem {
        private SimpleText simpleText;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class SimpleText {
        private String text;
    }

    /** 분석 결과 한 문장으로 simpleText 응답 생성 */
    public static KakaoSkillResponse ofText(String text) {
        return KakaoSkillResponse.builder()
            .version("2.0")
            .template(new SkillTemplate(List.of(
                new OutputItem(new SimpleText(text != null ? text : "응답을 생성하지 못했습니다."))
            )))
            .build();
    }
}
