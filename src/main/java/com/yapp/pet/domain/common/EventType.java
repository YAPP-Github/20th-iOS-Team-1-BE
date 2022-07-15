package com.yapp.pet.domain.common;

import lombok.Getter;

@Getter
public enum EventType {
    EVENT_SIGN_UP("회원가입 완료");

    private final String value;

    EventType(String value){
        this.value = value;
    }
}
