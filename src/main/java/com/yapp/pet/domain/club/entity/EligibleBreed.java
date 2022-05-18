package com.yapp.pet.domain.club.entity;

import lombok.Getter;

@Getter
public enum EligibleBreed {

    //추가 필요
    MALTESE("말티즈"),
    WELSH_CORGI("웰시코기"),
    RETRIEVER("리트리버"),
    ALL("상관없음");

    private final String value;

    EligibleBreed(String value){
        this.value = value;
    }

}
