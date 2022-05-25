package com.yapp.pet.domain.token.entity;

import lombok.Getter;

@Getter
public enum Social {

    APPLE("애플"),
    KAKAO("카카오");

    private final String value;

    Social(String value){
        this.value = value;
    }

}
