package com.yapp.pet.web.account.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "중복검사 응답 객체")
public class AccountValidationResponse {

    @Schema(title = "중복 검사 여부", description = "중복되지 않으면 true값을 리턴합니다.", example = "true")
    private boolean unique;

    @Schema(title = "길이 조건 만족 여부", description = "길이 조건을 만족하면 true값을 리턴합니다.", example = "true")
    private boolean satisfyLengthCondition;

}