package com.yapp.pet.domain.club.event;

import com.yapp.pet.domain.common.EventType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ClubDeletedEvent {

    private EventType type;

    private Long clubId;

    public static ClubDeletedEvent of(EventType type, Long clubId){
        return new ClubDeletedEvent(type, clubId);
    }

}
