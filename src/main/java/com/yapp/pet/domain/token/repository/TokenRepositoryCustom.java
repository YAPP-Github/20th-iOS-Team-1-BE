package com.yapp.pet.domain.token.repository;

import com.yapp.pet.domain.token.entity.Token;

import java.util.Optional;

public interface TokenRepositoryCustom {

    Optional<Token> findByUniqueIdentifier(String uniqueIdentifier);
}
