package com.yapp.pet.domain.pet.repository;

import com.yapp.pet.domain.pet.entity.Pet;

import java.util.List;

public interface PetRepositoryCustom {

    List<Pet> findPetsByAccountId(Long accountId);

}
