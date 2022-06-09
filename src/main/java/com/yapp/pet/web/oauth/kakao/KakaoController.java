package com.yapp.pet.web.oauth.kakao;

import com.yapp.pet.domain.account.AccountService;
import com.yapp.pet.domain.token.entity.Social;
import com.yapp.pet.web.oauth.apple.model.SignInResponse;
import com.yapp.pet.web.oauth.kakao.model.KakaoTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.yapp.pet.global.TogaetherConstants.*;

@RestController
@RequiredArgsConstructor
public class KakaoController {

    private final KakaoClient kakaoClient;

    private final AccountService accountService;

    @Value("${social.kakao.client-id}")
    private String clientId;

    @Value("${social.kakao.client-secret}")
    private String clientSecret;

    @GetMapping("/auth/kakao/callback")
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
