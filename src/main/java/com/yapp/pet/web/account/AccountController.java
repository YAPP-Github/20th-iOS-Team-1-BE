package com.yapp.pet.web.account;

import com.yapp.pet.domain.account.AccountService;
import com.yapp.pet.domain.account.entity.Account;
import com.yapp.pet.global.annotation.AuthAccount;
import com.yapp.pet.web.account.model.AccountSignUpRequest;
import com.yapp.pet.web.account.model.AccountValidationResponse;
import com.yapp.pet.web.account.model.MyPageResponse;
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
                                       @Valid @RequestPart AccountSignUpRequest accountSignUpRequest) {

        Long accountId;

        try {
            accountId = accountService.signUp(account, accountSignUpRequest);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        return ResponseEntity.ok(accountId);
    }

    @GetMapping("/accounts/my-page")
    public ResponseEntity<MyPageResponse> myPage(@AuthAccount Account account){

        MyPageResponse response;

        try {
            response = accountService.getMyPageInfo(account);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        return ResponseEntity.ok(response);
    }

}