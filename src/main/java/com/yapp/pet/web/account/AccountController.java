package com.yapp.pet.web.account;

import com.yapp.pet.domain.account.AccountService;
import com.yapp.pet.domain.account.entity.Account;
import com.yapp.pet.global.annotation.AuthAccount;
import com.yapp.pet.web.account.model.AccountSignUpRequest;
import com.yapp.pet.web.account.model.AccountValidationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "계정", description = "계정 관련 API를 제공합니다.")
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/accounts/validate-nickname")
    @Operation(summary = "닉네임 중복 체크", tags = "계정",
            description = "닉네임이 중복되면 true , 중복되지 않으면 false 값을 리턴합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = AccountValidationResponse.class)))
    })
    public ResponseEntity<AccountValidationResponse> validateNickname(String nickname){

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
    @Operation(summary = "회원가입", tags = "계정",
            description = "회원가입을 진행합니다. 계정의 고유 id값(pk)을 응답합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
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