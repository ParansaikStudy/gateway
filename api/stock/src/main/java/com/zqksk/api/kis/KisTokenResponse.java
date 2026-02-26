package com.zqksk.api.kis;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record KisTokenResponse(
    @JsonProperty("access_token") String accessToken,
    @JsonProperty("expires_in") long expiresIn
) {}
