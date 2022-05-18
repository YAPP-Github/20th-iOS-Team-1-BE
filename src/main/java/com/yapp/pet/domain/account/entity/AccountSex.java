package com.yapp.pet.domain.account.entity;

import lombok.Getter;

@Getter
public enum AccountSex {

    MAN("남자"),
    WOMAN("여자"),
    PRIVATE("비공개");

    private final String value;

    AccountSex(String value){
        this.value = value;
    }

}