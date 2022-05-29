package com.yapp.pet.domain.club.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.BooleanPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yapp.pet.domain.club.entity.Category;
import com.yapp.pet.global.util.DistanceUtil;
import com.yapp.pet.web.club.model.SearchingClubDto;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

import static com.yapp.pet.domain.account.entity.QAccount.account;
import static com.yapp.pet.domain.accountclub.entity.QAccountClub.accountClub;
import static com.yapp.pet.domain.club.entity.QClub.club;

@RequiredArgsConstructor
public class ClubRepositoryImpl implements ClubRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    private static final int PAGE_SIZE = 10;

    @Override
    public List<SearchingClubDto> searchClubByWord(String searchingWord, Double userLatitude, Double userLongitude, int page) {
        return queryFactory.select(Projections.constructor(SearchingClubDto.class, accountClub.club, accountClub.account.nickname, accountClub.club.accountClubs.size()))
                           .from(accountClub)
                           .join(accountClub.club, club)
                           .join(accountClub.account, account)
                           .where(isLeader(accountClub.leader))
                           .where(clubNameContains(searchingWord))
                           .offset(page)
                           .limit(PAGE_SIZE)
                           .fetch()
                           .stream()
                           .sorted(
                                   (club1, club2) -> {
                                       return DistanceUtil.getDistanceBetweenUserAndClub(
                                                                  userLatitude, club1.getLatitude(),
                                                                  userLongitude, club1.getLongitude())
                                                          .compareTo(
                                                                  DistanceUtil.getDistanceBetweenUserAndClub(
                                                                          userLatitude,
                                                                          club2.getLatitude(),
                                                                          userLongitude,
                                                                          club2.getLongitude()));
                                   }
                           )
                           .collect(Collectors.toList());
    }

    @Override
    public List<SearchingClubDto> searchClubByCategory(Category category, Double userLatitude, Double userLongitude, int page) {
        return queryFactory.select(Projections.constructor(SearchingClubDto.class, accountClub.club, accountClub.account.nickname, accountClub.club.accountClubs.size()))
                           .from(accountClub)
                           .join(accountClub.club, club)
                           .join(accountClub.account, account)
                           .where(isLeader(accountClub.leader))
                           .where(clubCategoryEq(category))
                           .offset(page)
                           .limit(PAGE_SIZE)
                           .fetch()
                           .stream()
                           .sorted(
                                   (club1, club2) -> {
                                       return DistanceUtil.getDistanceBetweenUserAndClub(
                                                                  userLatitude, club1.getLatitude(),
                                                                  userLongitude, club1.getLongitude())
                                                          .compareTo(
                                                                  DistanceUtil.getDistanceBetweenUserAndClub(
                                                                          userLatitude,
                                                                          club2.getLatitude(),
                                                                          userLongitude,
                                                                          club2.getLongitude()));
                                   }
                           )
                           .collect(Collectors.toList());
    }

    private BooleanExpression clubNameContains(String searchingWord) {
        return StringUtils.hasText(searchingWord) ? club.title.contains(searchingWord) : null;
    }

    private BooleanExpression clubCategoryEq(Category category) {
        return category == null ? null : club.category.eq(category);
    }

    private BooleanExpression isLeader(BooleanPath leader) {
        return leader.isTrue();
    }
}
