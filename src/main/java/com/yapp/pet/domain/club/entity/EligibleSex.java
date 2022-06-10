package com.yapp.pet.domain.club.entity;

import lombok.Getter;

@Getter
public enum EligibleSex {

    MAN("남자"),
    WOMAN("여자"),
    ALL("상관 없음");

    private final String value;

    EligibleSex(String value){
        this.value = value;
    }

}
