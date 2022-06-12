package com.yapp.pet.domain.common;

import lombok.Getter;

@Getter
public enum Category {

    WALK("산책"),
    DOG_CAFE("애견 카페"),
    PLAY_GROUND("놀이터"),
    DOG_FRIENDLY_RESTAURANT("애견 동반 식당"),
    EXPOSITION("박람회"),
    ETC("기타");

    private final  String value;

    Category(String value){
        this.value = value;
    }

}
