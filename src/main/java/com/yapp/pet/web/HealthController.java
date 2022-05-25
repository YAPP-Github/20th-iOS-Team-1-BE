package com.yapp.pet.web;

import com.yapp.pet.web.oauth.apple.model.TokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@Tag(name = "Health", description = "서버 Healthcheck를 위한 API")
public class HealthController {

    @GetMapping("/")
    @Operation(summary = "Healthcheck API", tags = "Health",
            description = "로드밸런서에서 EC2 서버 상태를 확인하기 위한 API 입니다. IOS에서 직접 호출하지는 않습니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    public ResponseEntity<HttpStatus> healthCheck(){
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
