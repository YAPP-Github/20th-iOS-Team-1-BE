package com.yapp.pet.web.oauth.apple.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import springfox.documentation.annotations.ApiIgnore;

@Getter
@Setter
@NoArgsConstructor
@ApiIgnore
public class CallbackResponseApple {

    private String state;

    private String code;

    @JsonProperty("id_token")
    private String idToken;

}
