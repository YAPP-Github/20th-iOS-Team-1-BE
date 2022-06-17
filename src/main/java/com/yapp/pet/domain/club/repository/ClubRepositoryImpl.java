package com.yapp.pet.domain.club.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.BooleanPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yapp.pet.domain.accountclub.AccountClub;
import com.yapp.pet.domain.common.Category;
import com.yapp.pet.domain.club.entity.Club;
import com.yapp.pet.domain.club.entity.ClubStatus;
import com.yapp.pet.domain.club.entity.EligibleSex;
import com.yapp.pet.domain.common.PetSizeType;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.yapp.pet.domain.accountclub.QAccountClub.accountClub;
import static com.yapp.pet.domain.club.entity.QClub.club;
import static com.yapp.pet.global.TogaetherConstants.ELIGIBLE_BREEDS_ALL;
import static com.yapp.pet.web.club.model.SearchingClubDto.SearchingRequest;
import static com.yapp.pet.web.club.model.SearchingWithinRangeClubDto.*;

@RequiredArgsConstructor
public class ClubRepositoryImpl implements ClubRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    private static final int PAGE_SIZE = 10;

    @Override
    public List<Club> searchClubByWord(SearchingRequest searchingRequest) {

        return queryFactory.selectFrom(accountClub)
                           .join(accountClub.club, club).fetchJoin()
                           .where(isLeader(accountClub.leader))
                           .where(clubNameContains(searchingRequest.getSearchingWord()))
                           .where(clubCategoryEq(searchingRequest.getCategory()))
                           .where(clubStatusEq(searchingRequest.getStatus()))
                           .where(clubPetTypeExist(searchingRequest.getEligibleBreed()))
                           .where(clubPetSizeTypeExist(searchingRequest.getPetSizeType()))
                           .where(clubEligibleSexEq(searchingRequest.getEligibleSex()))
                           .where(clubParticipateRange(searchingRequest.getParticipateMin(),
                                                       searchingRequest.getParticipateMax()))
                           .offset(searchingRequest.getPage())
                           .limit(PAGE_SIZE)
                           .fetch()
                           .stream()
                           .map(AccountClub::getClub)
                           .collect(Collectors.toList());
    }

    @Override
    public List<Club> searchClubByCategory(SearchingRequest searchingRequest) {

        return queryFactory.selectFrom(accountClub)
                           .join(accountClub.club, club).fetchJoin()
                           .where(isLeader(accountClub.leader))
                           .where(clubCategoryEq(searchingRequest.getCategory()))
                           .where(clubStatusEq(searchingRequest.getStatus()))
                           .where(clubPetTypeExist(searchingRequest.getEligibleBreed()))
                           .where(clubPetSizeTypeExist(searchingRequest.getPetSizeType()))
                           .where(clubEligibleSexEq(searchingRequest.getEligibleSex()))
                           .where(clubParticipateRange(searchingRequest.getParticipateMin(),
                                                       searchingRequest.getParticipateMax()))
                           .offset(searchingRequest.getPage())
                           .limit(PAGE_SIZE)
                           .fetch()
                           .stream()
                           .map(AccountClub::getClub)
                           .collect(Collectors.toList());
    }

    @Override
    public List<SearchingWithinRangeClubResponse> searchClubByWithinRange(SearchingWithinRangeClubRequest rangeRequest) {

        return queryFactory.select(
                                   Projections.constructor(SearchingWithinRangeClubResponse.class,
                                                           club.id, club.category, club.latitude, club.longitude))
                           .from(club)
                           .where(clubWithinRange(rangeRequest.getUpperLeftLatitude(),
                                                  rangeRequest.getUpperLeftLongitude(),
                                                  rangeRequest.getBottomRightLatitude(),
                                                  rangeRequest.getBottomRightLongitude()))
                           .fetch();
    }

    @Override
    public List<Club> findExceedTimeClub() {
        return queryFactory.selectFrom(club)
                           .where(clubStatusEq(ClubStatus.AVAILABLE).and(
                                   club.endDate.before(ZonedDateTime.now())))
                           .fetch();
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

    private BooleanExpression clubPetSizeTypeExist(PetSizeType petSizeType) {
        if (petSizeType == PetSizeType.ALL) {
            return null;
        }

        return petSizeType == null ? null : club.eligiblePetSizeTypes.any().eq(petSizeType);
    }

    private BooleanExpression clubPetTypeExist(String eligibleBreed) {
        if (eligibleBreed.equals(ELIGIBLE_BREEDS_ALL)) {
            return null;
        }

        return eligibleBreed == null ? null : club.eligibleBreeds.any().eq(eligibleBreed);
    }

    private BooleanExpression clubParticipateRange(Integer min, Integer max) {
        if(min == null || max == null) {
            return null;
        }

        return club.accountClubs.size().between(min, max);
    }

    private BooleanExpression clubStatusEq(ClubStatus clubStatus) {
        return clubStatus == null ? null : club.status.eq(clubStatus);
    }

    private Predicate clubEligibleSexEq(EligibleSex eligibleSex) {

        if (eligibleSex == null) {
            return null;
        }

        //유저가 고른 필터가 ALL일 경우에는 어떤 클럽이든 다 가능하다
        if (eligibleSex == EligibleSex.ALL) {
            return null;
        }

        //클럽의 조건 중에 모든 성별이 가능하다고 하면 유저가 고른 필터에 상관없다
        BooleanBuilder clubEligibleSexAll = new BooleanBuilder(club.eligibleSex.eq(EligibleSex.ALL)).and(null);

        //둘 다 아닐 경우에는 필터가 맞아야함
        BooleanBuilder clubEligibleSexEqFilter = new BooleanBuilder(club.eligibleSex.ne(EligibleSex.ALL)).and(club.eligibleSex.eq(eligibleSex));

        return new BooleanBuilder(clubEligibleSexAll).or(clubEligibleSexEqFilter);
    }

    private BooleanExpression clubWithinRange(Double upperLeftLatitude, Double upperLeftLongitude,
                                              Double bottomRightLatitude, Double bottomRightLongitude) {

        return club.latitude.between(bottomRightLatitude, upperLeftLatitude)
                            .and(club.longitude.between(upperLeftLongitude, bottomRightLongitude));
    }
}
