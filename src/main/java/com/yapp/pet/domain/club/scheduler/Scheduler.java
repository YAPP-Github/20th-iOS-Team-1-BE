package com.yapp.pet.domain.club.scheduler;

import com.yapp.pet.domain.club.entity.Club;
import com.yapp.pet.domain.club.entity.ClubStatus;
import com.yapp.pet.domain.club.repository.jpa.ClubRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class Scheduler {

    private final ClubRepository clubRepository;

    @Scheduled(cron = "0 0/10 * * * *")
    public void exceedClub() {
        List<Club> exceedTimeClub = clubRepository.findExceedTimeClub();

        if (exceedTimeClub.isEmpty()) {
            return;
        }

        clubRepository.findExceedTimeClub().forEach(club -> {
            log.info("모임 종료, id : {}", club.getId());
            club.updateStatus(ClubStatus.END);
        });

        List<Long> ids = exceedTimeClub.stream()
                .map(Club::getId)
                .collect(Collectors.toList());

        clubRepository.updateStatusToEndClub(ids, ClubStatus.END);
    }
}
