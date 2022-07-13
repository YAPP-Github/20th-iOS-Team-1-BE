package com.yapp.pet.domain.pet.repository;

import com.yapp.pet.domain.pet.entity.Pet;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface PetRepository extends JpaRepository<Pet, Long>, PetRepositoryCustom {
    @Override
    @EntityGraph(attributePaths = {"tags"})
    Optional<Pet> findById(Long id);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("delete from Pet p where p.id in :ids")
    void deletePetById(@Param("ids") List<Long> ids);
}
