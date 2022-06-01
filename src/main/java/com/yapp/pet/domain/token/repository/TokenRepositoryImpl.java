package com.yapp.pet.domain.token.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yapp.pet.domain.account.entity.QAccount;
import com.yapp.pet.domain.token.entity.Token;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.yapp.pet.domain.token.entity.QToken.token;

@RequiredArgsConstructor
public class TokenRepositoryImpl implements TokenRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Token> findByUniqueIdBySocial(String uniqueIdBySocial) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(token)
                        .join(token.account, QAccount.account).fetchJoin()
                        .where(token.uniqueIdBySocial.eq(uniqueIdBySocial))
                        .fetchOne()
        );
    }

}
