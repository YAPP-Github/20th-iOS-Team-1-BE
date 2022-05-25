package com.yapp.pet.web.oauth.apple.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "소셜 로그인 응답 객체")
public class TokenResponse {

    @Schema(title = "access token", description = "인증 토큰",
            example = "eyhidfuiiasdjivxJasiC.etjvcxJxodfo93fjixCVKcfkld.RKOFD045fdBkzls3Q")
    private String accessToken;

    @Schema(title = "refresh token", description = "access token 재발급을 위한 토큰",
            example = "fdsdfu9fjcXjivxJasiC.ezZFD5nopjJ453fxfji6FCjVKcfkld.fC2dFD04kzls4V")
    private String refreshToken;

    @Schema(title = "계정 존재 여부", description = "이미 존재하는 회원인지 여부 확인", example = "true")
    private boolean isFirstAccount;

    public void addToken(String accessToken, String refreshToken){
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

}
