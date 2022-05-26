package com.yapp.pet.web.oauth.apple;

import com.yapp.pet.domain.account.service.AccountService;
import com.yapp.pet.domain.token.entity.Social;
import com.yapp.pet.web.oauth.apple.model.CallbackResponseApple;
import com.yapp.pet.web.oauth.apple.model.TokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "소셜로그인", description = "애플 또는 카카오를 통해 로그인 및 회원가입을 진행합니다.")
public class AppleController {

    private final AccountService accountService;

    @PostMapping("/auth/apple/callback")
    @Operation(summary = "애플 콜백 API", tags = "소셜로그인",
            description = "애플에서 Togaether 서버로 콜백하는 API 입니다. IOS에서 직접 호출하지는 않습니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                content = @Content(schema = @Schema(implementation = TokenResponse.class)))
    })
    public ResponseEntity<TokenResponse> callbackOfApple(@RequestBody CallbackResponseApple callbackResponse){
        TokenResponse tokenResponse = null;

        if (callbackResponse == null) {
            return null; // Todo 상세 처리 필요
        }

        try {
            tokenResponse = accountService.signIn(callbackResponse.getIdToken(), Social.APPLE);

        } catch (Exception e) {
            // Todo 예외 구체화
        }

        return ResponseEntity.ok(tokenResponse);
    }

}
