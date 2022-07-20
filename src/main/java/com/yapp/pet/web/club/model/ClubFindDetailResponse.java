package com.yapp.pet.web.club.model;

import com.yapp.pet.domain.account.entity.Account;
import com.yapp.pet.domain.accountclub.AccountClub;
import com.yapp.pet.domain.club.entity.EligibleSex;
import com.yapp.pet.domain.common.Category;
import com.yapp.pet.domain.common.PetSizeType;
import com.yapp.pet.web.comment.model.CommentResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EntityNotFoundException;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class ClubFindDetailResponse {

    private boolean participating;

    private boolean leader;

    private ClubDetailInfo clubDetailInfo;

    private AccountInfo leaderInfo;

    private List<AccountInfo> accountInfos;

    private List<CommentResponse> commentInfos;

    @Builder
    public ClubFindDetailResponse(List<AccountClub> accountClubs, Account loginAccount, List<CommentResponse> commentInfos,
                                  ClubDetailInfo clubDetailInfo){
        this.participating = accountClubs.stream()
                .anyMatch(ac -> ac.getAccount().equals(loginAccount));

        this.clubDetailInfo = clubDetailInfo;

        Account leaderAccount = accountClubs.stream()
                .filter(AccountClub::isLeader)
                .map(AccountClub::getAccount)
                .findFirst()
                .orElseThrow(EntityNotFoundException::new);

        this.leader = leaderAccount.equals(loginAccount);

        this.leaderInfo = new AccountInfo(leaderAccount);

        this.accountInfos = accountClubs.stream()
                .map(AccountClub::getAccount)
                .map(AccountInfo::new)
                .collect(Collectors.toList());

        this.commentInfos = commentInfos;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class ClubDetailInfo{
        private Long id;
        private String title;
        private String description;
        private Category category;
        private String meetingPlace;
        private Double latitude;
        private Double longitude;
        private ZonedDateTime startDate;
        private ZonedDateTime endDate;
        private Set<String> eligibleBreeds = new HashSet<>();
        private Set<PetSizeType> eligiblePetSizeTypes = new HashSet<>();
        private EligibleSex eligibleSex;
        private int maximumPeople;
        private int participants;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class AccountInfo{
        private Long id;
        private String nickname;
        private String imageUrl;

        public AccountInfo(Account account){
            this.id = account.getId();
            this.nickname = account.getNickname();

            if (account.getAccountImage().getPath() != null) {
                this.imageUrl = account.getAccountImage().getPath();
            }
        }
    }

}
