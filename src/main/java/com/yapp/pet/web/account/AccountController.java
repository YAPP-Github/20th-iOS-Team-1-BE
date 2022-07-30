package com.yapp.pet.web.account;

import com.yapp.pet.domain.account.entity.Account;
import com.yapp.pet.domain.account.service.AccountQueryService;
import com.yapp.pet.domain.account.service.AccountService;
import com.yapp.pet.global.annotation.AuthAccount;
import com.yapp.pet.web.account.model.AccountSignUpRequest;
import com.yapp.pet.web.account.model.AccountUpdateRequest;
import com.yapp.pet.web.account.model.AccountValidationResponse;
import com.yapp.pet.web.account.model.MyPageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AccountController {

    private final AccountService accountService;
    private final AccountQueryService accountQueryService;

    @GetMapping("/accounts/validation/{nickname}")
    public ResponseEntity<AccountValidationResponse> validateNickname(@PathVariable("nickname") String nickname){

        AccountValidationResponse response = accountQueryService.validateNickname(nickname);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/accounts/sign-up")
    public ResponseEntity<Long> signUp(@AuthAccount Account account,
                                       @Valid @ModelAttribute AccountSignUpRequest accountSignUpRequest) {

        Long accountId = accountService.signUp(account, accountSignUpRequest);

        return ResponseEntity.ok(accountId);
    }

    @GetMapping("/accounts/my-page")
    public ResponseEntity<MyPageResponse> myPage(@RequestParam(value = "nickname", required = false) String nickname,
                                                 @AuthAccount Account account){

        MyPageResponse response = accountQueryService.getMyPageInfo(account, nickname);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/accounts")
    public ResponseEntity<Long> updateAccount(@AuthAccount Account account,
                                              @ModelAttribute AccountUpdateRequest accountUpdateRequest){

        Long accountId = accountService.updateAccount(account, accountUpdateRequest);

        return ResponseEntity.ok(accountId);
    }

    @DeleteMapping("/accounts")
    public ResponseEntity<Long> deleteAccount(@AuthAccount Account account) {

        Long accountId = accountService.delete(account);

        return ResponseEntity.ok(accountId);
    }

}