package com.zqksk.api.domain.user.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record ScreenRequest(

        @Schema(description = "id", example = "1", type = "number")
        Long id,

        @Schema(description = "parentId", example = "1", type = "number")
        Long parentId,

        @NotBlank
        @Schema(description = "화면명", example = "설치정보", maxLength = 100, type = "string")
        String name,

        @NotBlank
        @Schema(description = "컴포넌트명", example = "pc", maxLength = 100, type = "string")
        String componentName,

        @NotBlank
        @Schema(description = "url", example = "/pc-page", maxLength = 500, type = "string")
        String url


) {

    public NewScreen toNewScreen(){
        return new NewScreen(
                id,
                parentId,
                name,
                componentName,
                url
        );
    }
}
