package com.yapp.pet.domain.club.scheduler;

import com.yapp.pet.domain.club.service.ClubQueryService;
import com.yapp.pet.domain.club.entity.ClubStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Scheduler {

    private final ClubQueryService clubQueryService;

    @Scheduled(cron = "0 0/10 * * * *")
    public void exceedClub() {
        clubQueryService.exceedTimeClub()
                        .forEach(club -> {
                       club.updateStatus(ClubStatus.END);
                   });
    }
}
