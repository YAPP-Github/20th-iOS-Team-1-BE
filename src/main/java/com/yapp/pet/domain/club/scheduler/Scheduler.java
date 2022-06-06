package com.yapp.pet.domain.club.scheduler;

import com.yapp.pet.domain.club.ClubService;
import com.yapp.pet.domain.club.entity.ClubStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Scheduler {

    private final ClubService clubService;

    @Scheduled(cron = "0 * * * * *")
    public void exceedClub() {
        clubService.exceedTimeClub()
                   .forEach(club -> {
                       club.updateStatus(ClubStatus.END);
                   });
    }
}
