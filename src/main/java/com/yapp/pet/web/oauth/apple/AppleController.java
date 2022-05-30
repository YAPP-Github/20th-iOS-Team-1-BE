package com.yapp.pet.web.oauth.apple;

import com.yapp.pet.domain.account.AccountService;
import com.yapp.pet.domain.token.entity.Social;
import com.yapp.pet.web.oauth.apple.model.CallbackResponseApple;
import com.yapp.pet.web.oauth.apple.model.SignInResponse;
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
    @Operation(summary = "애플 로그인 API", tags = "소셜로그인",
            description = "IOS에서 애플 서버로 로그인 요청 시, 애플에서 Togaether 서버로 콜백 후, " +
                    "회원가입 여부와 함께 토큰 값을 응답합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                content = @Content(schema = @Schema(implementation = SignInResponse.class)))
    })
    public ResponseEntity<SignInResponse> callbackOfApple(@RequestBody CallbackResponseApple callbackResponse){
        SignInResponse signInResponse = null;

        if (callbackResponse == null) {
            return null; // Todo 상세 처리 필요
        }

        try {
            signInResponse = accountService.signIn(callbackResponse.getIdToken(), Social.APPLE);

        } catch (Exception e) {
            // Todo 예외 구체화
        }

        return ResponseEntity.ok(signInResponse);
    }

}
