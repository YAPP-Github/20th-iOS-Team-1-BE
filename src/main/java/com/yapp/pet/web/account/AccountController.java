package com.yapp.pet.web.account;

import com.yapp.pet.domain.account.AccountService;
import com.yapp.pet.domain.account.entity.Account;
import com.yapp.pet.global.annotation.AuthAccount;
import com.yapp.pet.web.account.model.AccountSignUpRequest;
import com.yapp.pet.web.account.model.AccountValidationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/accounts/validate-nickname/{nickname}")
    public ResponseEntity<AccountValidationResponse> validateNickname(@PathVariable("nickname") String nickname){

        AccountValidationResponse response;

        try {
            response = accountService.validateNickname(nickname);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/accounts/sign-up")
    public ResponseEntity<Long> signUp(@AuthAccount Account account,
                                       @RequestPart(value = "imageFile", required = false) MultipartFile imageFile,
                                       @Valid @RequestPart AccountSignUpRequest accountSignUpRequest) {

        Long accountId;

        try {
            accountId = accountService.signUp(account, accountSignUpRequest, imageFile);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        return ResponseEntity.ok(accountId);
    }

}