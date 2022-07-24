package com.yapp.pet.domain.club.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ClubDeletedEvent {

    private Long clubId;

    public static ClubDeletedEvent from(Long clubId){
        return new ClubDeletedEvent(clubId);
    }

}
