package com.yapp.pet.web.club.model;

import lombok.Getter;

@Getter
public enum ClubParticipateRejectReason {

    HAS_NOT_PET("반려견 정보 없음"),
    NOT_ELIGIBLE_PET_SIZE_TYPE("참여 불가능한 반려견 크기"),
    NOT_ELIGIBLE_BREEDS("참여 불가능한 견종"),
    NOT_ELIGIBLE_SEX("참여 불가능한 견주 성별"),
    FULL("인원 마감");

    private final String value;

    ClubParticipateRejectReason(String value){
        this.value = value;
    }
}
