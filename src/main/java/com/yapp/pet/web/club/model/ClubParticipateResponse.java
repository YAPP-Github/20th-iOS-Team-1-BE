package com.yapp.pet.web.club.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ClubParticipateResponse {

    private boolean eligible;

    private ClubParticipateRejectReason rejectReason;

    private Long clubId;

    private Long accountClubId;

    public ClubParticipateResponse(boolean eligible, ClubParticipateRejectReason rejectReason) {
        this.eligible = eligible;
        this.rejectReason = rejectReason;
    }

    public static ClubParticipateResponse of(boolean eligible, ClubParticipateRejectReason rejectReason){
        return new ClubParticipateResponse(eligible, rejectReason);
    }
}
