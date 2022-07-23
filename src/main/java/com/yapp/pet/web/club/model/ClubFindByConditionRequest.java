package com.yapp.pet.web.club.model;

import com.yapp.pet.domain.club.repository.ClubFindCondition;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ClubFindByConditionRequest {

    private Long cursorId;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime cursorEndDate;

    private ClubFindCondition condition;

    @Builder
    public ClubFindByConditionRequest(Long cursorId, LocalDateTime cursorEndDate, ClubFindCondition condition) {
        this.cursorId = cursorId;
        this.cursorEndDate = cursorEndDate;
        this.condition = condition;
    }
}
