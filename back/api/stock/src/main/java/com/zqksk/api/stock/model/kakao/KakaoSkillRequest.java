package com.zqksk.api.stock.model.kakao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 카카오 i 오픈빌더 스킬 요청 body.
 * userRequest.utterance 에 사용자 발화가 담겨 옵니다.
 */
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoSkillRequest {

    private UserRequest userRequest;

    @Getter
    @Setter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UserRequest {
        private String utterance;
    }
}
