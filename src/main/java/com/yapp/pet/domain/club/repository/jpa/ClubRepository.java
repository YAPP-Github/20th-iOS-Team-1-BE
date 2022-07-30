package com.yapp.pet.domain.club.repository.jpa;

import com.yapp.pet.domain.club.entity.Club;
import com.yapp.pet.domain.club.entity.ClubStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

public interface ClubRepository extends JpaRepository<Club, Long>, ClubRepositoryCustom {

    @Override
    @EntityGraph(attributePaths = {"accountClubs", "eligiblePetSizeTypes", "eligibleBreeds"})
    Optional<Club> findById(Long id);

    @Modifying
    @Transactional
    @Query("update Club c set c.status = :status where c.id in :ids")
    void updateStatusToEndClub(@Param("ids") List<Long> ids, @Param("status") ClubStatus status);

    default Club findByIdWrapper(Long clubId) {
        return findById(clubId).orElseThrow(EntityNotFoundException::new);
    }

}
