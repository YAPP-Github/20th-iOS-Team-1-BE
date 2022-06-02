package com.yapp.pet.web.oauth.kakao.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CallbackResponseKakao {

    private String code;

    private String state;

    private String error;

    @JsonProperty("error_description")
    private String errorDescription;

}
