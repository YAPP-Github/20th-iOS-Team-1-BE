package com.yapp.pet.domain.common;

import lombok.Getter;

@Getter
public enum EventType {
    EVENT_SIGNED_UP("회원가입 완료"),
    EVENT_CLUB_DELETED("모임 삭제"),
    EVENT_COMMENT_DELETED("댓글 삭제"),
    EVENT_ACCOUNT_DELETED("회원 삭제(탈퇴처리)");

    private final String value;

    EventType(String value){
        this.value = value;
    }
}
