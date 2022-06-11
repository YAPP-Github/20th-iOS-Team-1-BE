package com.yapp.pet.domain.pet.repository;

import com.yapp.pet.domain.pet.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet, Long>, PetRepositoryCustom {
}
