package com.yapp.pet.web.oauth.apple.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class SignInResponse {

    private String accessToken;

    private String refreshToken;

    private Boolean firstAccount;

    public void addToken(String accessToken, String refreshToken){
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

}
