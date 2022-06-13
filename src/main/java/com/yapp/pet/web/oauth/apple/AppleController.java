package com.yapp.pet.web.oauth.apple;

import com.yapp.pet.domain.account.service.AccountService;
import com.yapp.pet.domain.token.entity.Social;
import com.yapp.pet.web.oauth.apple.model.CallbackResponseApple;
import com.yapp.pet.web.oauth.apple.model.SignInResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AppleController {

    private final AccountService accountService;

    @PostMapping("/auth/apple/callback")
    public ResponseEntity<SignInResponse> callbackOfApple(@RequestBody CallbackResponseApple callbackResponse){

        SignInResponse signInResponse;

        try {
            signInResponse = accountService.signIn(callbackResponse.getIdToken(), Social.APPLE);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        return ResponseEntity.ok(signInResponse);
    }

}
