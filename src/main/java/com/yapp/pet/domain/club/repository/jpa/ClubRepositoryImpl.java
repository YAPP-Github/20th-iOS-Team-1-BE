package com.yapp.pet.domain.club.repository.jpa;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yapp.pet.domain.account.entity.Account;
import com.yapp.pet.domain.accountclub.AccountClub;
import com.yapp.pet.domain.club.entity.Club;
import com.yapp.pet.domain.club.entity.ClubStatus;
import com.yapp.pet.domain.club.entity.EligibleSex;
import com.yapp.pet.domain.club.repository.ClubFindCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import javax.persistence.LockModeType;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.yapp.pet.domain.account.entity.QAccount.account;
import static com.yapp.pet.domain.accountclub.QAccountClub.accountClub;
import static com.yapp.pet.domain.club.entity.QClub.club;
import static com.yapp.pet.domain.club.repository.ClubFindCondition.*;
import static com.yapp.pet.web.club.model.SearchingWithinRangeClubDto.SearchingWithinRangeClubRequest;
import static com.yapp.pet.web.club.model.SearchingWithinRangeClubDto.SearchingWithinRangeClubResponse;

@RequiredArgsConstructor
public class ClubRepositoryImpl implements ClubRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    private static final int PAGE_SIZE = 10;

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

    @Override
    public Page<Club> findClubsByCondition(String customCursor, ClubFindCondition condition,
                                           Account loginAccount, Pageable pageable) {

        List<AccountClub> findAccountClubs = queryFactory.selectFrom(accountClub)
                .innerJoin(accountClub.account, account).fetchJoin()
                .innerJoin(accountClub.club, club).fetchJoin()
                .leftJoin(club.eligibleBreeds).fetchJoin()
                .innerJoin(club.eligiblePetSizeTypes).fetchJoin()
                .where(
                        customCursor(customCursor),
                        isParticipating(condition, loginAccount),
                        isLeader(condition, loginAccount),
                        isParticipatedAndExceed(condition, loginAccount)
                )
                .limit(pageable.getPageSize())
                .fetch();

        List<Club> findClubs = findAccountClubs.stream()
                .map(AccountClub::getClub)
                .collect(Collectors.toList());

        return PageableExecutionUtils.getPage(findClubs, pageable, findClubs::size);
    }

    @Override
    public Optional<Club> findClubDetailById(Long clubId) {

        AccountClub findAccountClub = queryFactory
                .selectFrom(accountClub)
                .innerJoin(accountClub.account, account).fetchJoin()
                .innerJoin(accountClub.club, club).fetchJoin()
                .innerJoin(club.eligiblePetSizeTypes).fetchJoin()
                .leftJoin(club.eligibleBreeds).fetchJoin()
                .where(club.id.eq(clubId))
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .fetchFirst();

        return Optional.ofNullable(findAccountClub.getClub());
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

    private BooleanExpression customCursor(String customCursor){
        if (customCursor == null) {
            return null;
        }

        StringTemplate stringTemplate = Expressions.stringTemplate(
                "DATE_FORMAT({0}, {1})",
                club.endDate,
                ConstantImpl.create("%Y%m%d%H%i%s")
        );

        return StringExpressions.lpad(stringTemplate, 20, '0')
                .concat(StringExpressions.lpad(club.id.stringValue(), 10, '0'))
                .gt(customCursor);
    }

    private BooleanExpression isParticipating(ClubFindCondition condition, Account account){
        if (condition == null || !condition.equals(I_AM_PARTICIPATING)) {
            return null;
        }

        return accountClub.account.eq(account)
                                  .and(accountClub.club.status.ne(ClubStatus.END));
    }

    private BooleanExpression isLeader(ClubFindCondition condition, Account account){
        if (condition == null || !condition.equals(I_AM_LEADER)) {
            return null;
        }

        return accountClub.account.eq(account)
                                  .and(accountClub.leader.isTrue())
                                  .and(accountClub.club.status.ne(ClubStatus.END));
    }

    private BooleanExpression isParticipatedAndExceed(ClubFindCondition condition, Account account){
        if (condition == null || !condition.equals(I_AM_PARTICIPATED_AND_EXCEED)) {
            return null;
        }

        return accountClub.account.eq(account)
                                  .and(accountClub.club.status.eq(ClubStatus.END));
    }
}
