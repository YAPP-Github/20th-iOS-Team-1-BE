package com.yapp.pet.domain.pet.entity;

import lombok.Getter;

@Getter
public enum PetSex {

    MALE("수컷"),
    FEMALE("암컷");

    private final String value;

    PetSex(String value){
        this.value = value;
    }

}
