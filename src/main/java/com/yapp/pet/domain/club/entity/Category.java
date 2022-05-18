package com.yapp.pet.domain.club.entity;

import lombok.Getter;

@Getter
public enum Category {

    // 추가 필요
    WALK("산책"),
    DOG_CAFE("애견 카페");


    private final  String value;

    Category(String value){
        this.value = value;
    }

}
