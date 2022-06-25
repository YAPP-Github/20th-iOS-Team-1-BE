package com.yapp.pet.web.oauth.apple;

import com.yapp.pet.domain.account.service.AccountService;
import com.yapp.pet.domain.token.entity.Social;
import com.yapp.pet.web.oauth.apple.model.CallbackResponseApple;
import com.yapp.pet.web.oauth.apple.model.SignInResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AppleController {

    private final AccountService accountService;

    @PostMapping("/auth/apple/callback")
    public ResponseEntity<SignInResponse> callbackOfApple(@RequestBody CallbackResponseApple callbackResponse){

        log.info("애플 redirect, {}", callbackResponse.toString());

        SignInResponse signInResponse;

        try {
            signInResponse = accountService.signIn(callbackResponse.getIdToken(), Social.APPLE);

            log.info("응답, {}", signInResponse.toString());
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }



        return ResponseEntity.ok(signInResponse);
    }

}
