package com.yapp.pet.web.oauth.kakao;

import com.yapp.pet.domain.account.AccountService;
import com.yapp.pet.domain.token.entity.Social;
import com.yapp.pet.web.oauth.apple.model.SignInResponse;
import com.yapp.pet.web.oauth.kakao.model.KakaoTokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.yapp.pet.global.TogaetherConstants.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "소셜로그인", description = "애플 또는 카카오를 통해 로그인 및 회원가입을 진행합니다.")
public class KakaoController {

    private final KakaoClient kakaoClient;

    private final AccountService accountService;

    @Value("${social.kakao.client-id}")
    private String clientId;

    @Value("${social.kakao.client-secret}")
    private String clientSecret;

    @GetMapping("/auth/kakao/callback")
    @Operation(summary = "카카오 로그인 API", tags = "소셜로그인",
            description = "IOS에서 카카오 서버로 로그인 요청 시, 카카오에서 Togaether 서버로 콜백 후, " +
                    "회원가입 여부와 함께 토큰 값을 응답합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = SignInResponse.class)))
    })
    public ResponseEntity<SignInResponse> callbackOfKakao(String code){
        SignInResponse signInResponse;

        KakaoTokenResponse kakaoTokenResponse
                = kakaoClient.requestKakaoToken(KAKAO_CONTENT_TYPE, GRANT_TYPE,
                                                clientId, DEV_REDIRECT_URI, code, clientSecret);

        try {
            signInResponse = accountService.signIn(kakaoTokenResponse.getIdToken(), Social.KAKAO);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        return ResponseEntity.ok(signInResponse);
    }

}
