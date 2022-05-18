package com.yapp.pet.domain.common;

import lombok.Getter;

@Getter
public enum PetSizeType {

    LARGE("대형견"),
    MEDIUM("중형견"),
    SMALL("소형견");

    private final String value;

    PetSizeType(String value){
        this.value = value;
    }

}
