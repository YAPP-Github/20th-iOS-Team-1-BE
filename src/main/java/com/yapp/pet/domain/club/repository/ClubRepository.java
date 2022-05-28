package com.yapp.pet.domain.club.repository;

import com.yapp.pet.domain.club.entity.Club;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubRepository extends JpaRepository<Club, Long>, ClubRepositoryCustom {
}
