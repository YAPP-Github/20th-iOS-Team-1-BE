package com.yapp.pet.web.token;

import com.yapp.pet.domain.account.entity.Account;
import com.yapp.pet.domain.token.TokenService;
import com.yapp.pet.global.annotation.AuthAccount;
import com.yapp.pet.web.token.model.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TokenController {

    private final TokenService tokenService;

    @PostMapping("/tokens/re-issuance")
    public ResponseEntity<TokenResponse> reIssuance(@AuthAccount Account account) {
        TokenResponse tokenResponse = tokenService.reIssuance(account);

        return ResponseEntity.ok(tokenResponse);
    }

    @DeleteMapping("/tokens/expire")
    public ResponseEntity<Long> expireRefreshToken(@AuthAccount Account account) {
        Long deletedTokenId = tokenService.expireRefreshToken(account);

        return ResponseEntity.ok(deletedTokenId);
    }

}
