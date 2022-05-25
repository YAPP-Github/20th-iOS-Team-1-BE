package com.yapp.pet.domain.token.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yapp.pet.domain.token.entity.Token;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.yapp.pet.domain.token.entity.QToken.token;

@RequiredArgsConstructor
public class TokenRepositoryImpl implements TokenRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Token> findByUniqueIdentifier(String uniqueIdentifier) {
        return Optional.ofNullable(
                queryFactory
                    .selectFrom(token)
                    .where(token.uniqueIdentifier.eq(uniqueIdentifier))
                    .fetchOne()
        );
    }

}
