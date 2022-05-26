package com.yapp.pet.web.token;

import com.yapp.pet.domain.token.TokenService;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "토큰", description = "토큰 관련 API를 제공합니다.")
public class TokenController {

    private final TokenService tokenService;

    @PostMapping("/token/re-issuance")
    @Operation(summary = "access token 재발급", tags = "토큰",
            description = "access token 만료시, refresh token을 통해 재발급받습니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = TokenResponse.class)))
    })
    public ResponseEntity<TokenResponse> reIssuance(HttpServletRequest httpRequest) {
        TokenResponse tokenResponse = tokenService.reIssuance(httpRequest);

        return ResponseEntity.ok(tokenResponse);
    }

}
