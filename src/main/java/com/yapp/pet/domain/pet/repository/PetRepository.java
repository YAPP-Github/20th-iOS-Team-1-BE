package com.yapp.pet.domain.pet.repository;

import com.yapp.pet.domain.pet.entity.Pet;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PetRepository extends JpaRepository<Pet, Long>, PetRepositoryCustom {
    @Override
    @EntityGraph(attributePaths = {"tags"})
    Optional<Pet> findById(Long id);
}
