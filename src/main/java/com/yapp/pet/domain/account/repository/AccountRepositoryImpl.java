package com.yapp.pet.domain.account.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yapp.pet.domain.account.entity.Account;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.yapp.pet.domain.account.entity.QAccount.account;
import static com.yapp.pet.domain.account_image.QAccountImage.accountImage;
import static com.yapp.pet.domain.token.entity.QToken.token;

@RequiredArgsConstructor
public class AccountRepositoryImpl implements AccountRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Account> findByNickname(String nickname) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(account)
                        .distinct()
                        .join(account.token, token).fetchJoin()
                        .leftJoin(account.accountImage, accountImage).fetchJoin()
                        .leftJoin(account.interestCategories).fetchJoin()
                        .where(account.nickname.eq(nickname))
                        .fetchOne()
        );
    }

}
