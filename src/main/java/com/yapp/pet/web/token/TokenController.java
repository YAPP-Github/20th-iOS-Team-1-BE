package com.yapp.pet.web.token;

import com.yapp.pet.domain.account.entity.Account;
import com.yapp.pet.domain.token.TokenService;
import com.yapp.pet.global.annotation.AuthAccount;
import com.yapp.pet.global.exception.common.response.ExceptionResponseInfo;
import com.yapp.pet.web.token.model.TokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "토큰", description = "토큰 관련 API를 제공합니다.")
public class TokenController {

    private final TokenService tokenService;

    @PostMapping("/tokens/re-issuance")
    @Operation(summary = "access token 재발급", tags = "토큰",
            description = "access token 만료시, refresh token을 통해 재발급받습니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = TokenResponse.class))),
            @ApiResponse(responseCode = "401", description = "잘못된 토큰인 경우",
                    content = @Content(schema = @Schema(implementation = ExceptionResponseInfo.class)))
    })
    public ResponseEntity<TokenResponse> reIssuance(@AuthAccount Account account) {
        TokenResponse tokenResponse = tokenService.reIssuance(account);

        return ResponseEntity.ok(tokenResponse);
    }

    @DeleteMapping("/tokens/expire")
    @Operation(summary = "로그아웃", tags = "토큰",
            description = "refresh token 삭제를 통해 로그아웃 합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "잘못된 토큰인 경우",
                    content = @Content(schema = @Schema(implementation = ExceptionResponseInfo.class)))
    })
    public ResponseEntity<HttpStatus> expireRefreshToken(@AuthAccount Account account) {
        tokenService.expireRefreshToken(account);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
