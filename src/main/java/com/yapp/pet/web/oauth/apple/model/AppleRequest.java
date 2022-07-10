package com.yapp.pet.web.oauth.apple.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class AppleRequest {

    @NotBlank
    private String idToken;

    private String email;

}
