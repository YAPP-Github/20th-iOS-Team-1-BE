package com.yapp.pet.domain.pet.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yapp.pet.domain.pet.entity.Pet;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.yapp.pet.domain.pet.entity.QPet.pet;
import static com.yapp.pet.domain.pet_tag.QPetTag.petTag;

@RequiredArgsConstructor
public class PetRepositoryImpl implements PetRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Pet> findPetsByAccountId(Long accountId) {
        return queryFactory
                .selectFrom(pet)
                .distinct()
                .leftJoin(pet.tags, petTag).fetchJoin()
                .where(pet.account.id.eq(accountId))
                .fetch();
    }
}
