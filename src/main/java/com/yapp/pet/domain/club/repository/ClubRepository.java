package com.yapp.pet.domain.club.repository;

import com.yapp.pet.domain.club.entity.Club;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClubRepository extends JpaRepository<Club, Long>, ClubRepositoryCustom {

    @Override
    @EntityGraph(attributePaths = {"accountClubs", "eligiblePetSizeTypes", "eligibleBreeds"})
    Optional<Club> findById(Long id);
}
