package com.yapp.pet.domain.club.repository;

import lombok.Getter;

@Getter
public enum ClubFindCondition {

    I_AM_PARTICIPATING("내가 참여중인"),
    I_AM_LEADER("내가 방장"),
    I_AM_PARTICIPATED_AND_EXCEED("내가 참여했고 종료된"),
    PLACE_SEOUL("서울"),
    PLACE_JEJU_ISLAND("제주도"),
    AVAILABLE("참여 가능"),
    PERSONNEL_FULL("인원 마감"),
    END("종료");

    private final String value;

    ClubFindCondition(String value){
        this.value = value;
    }
}
