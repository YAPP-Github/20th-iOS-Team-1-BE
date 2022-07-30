package com.yapp.pet.domain.club.scheduler;

import com.yapp.pet.domain.club.entity.ClubStatus;
import com.yapp.pet.domain.club.repository.jpa.ClubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Scheduler {

    private final ClubRepository clubRepository;

    @Scheduled(cron = "0 0/10 * * * *")
    public void exceedClub() {
        clubRepository.findExceedTimeClub()
                        .forEach(club -> {
                       club.updateStatus(ClubStatus.END);
                   });
    }
}
