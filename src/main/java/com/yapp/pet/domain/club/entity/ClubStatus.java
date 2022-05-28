package com.yapp.pet.domain.club.entity;

import lombok.Getter;

@Getter
public enum ClubStatus {

    AVAILABLE("참여 가능"),
    PERSONNEL_FULL("인원 마감"),
    END("종료");

    private final String value;

    ClubStatus(String value){
        this.value = value;
    }
}
