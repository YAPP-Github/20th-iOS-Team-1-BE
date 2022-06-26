package com.yapp.pet.web.oauth.apple;

import com.yapp.pet.domain.account.service.AccountService;
import com.yapp.pet.domain.token.entity.Social;
import com.yapp.pet.web.oauth.apple.model.AppleRequest;
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

    @PostMapping("/auth/apple")
    public ResponseEntity<SignInResponse> appleLogin(@RequestBody AppleRequest appleRequest){

        SignInResponse signInResponse;

        try {
            signInResponse = accountService.signIn(appleRequest.getIdToken(), Social.APPLE);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        return ResponseEntity.ok(signInResponse);
    }

}
