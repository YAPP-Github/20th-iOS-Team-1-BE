package com.yapp.pet.domain.accountclub;

import com.yapp.pet.domain.account.entity.Account;
import com.yapp.pet.domain.club.entity.Club;
import com.yapp.pet.domain.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccountClub extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_club_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    private Club club;

    @Column(columnDefinition = "TINYINT(1) UNSIGNED DEFAULT 0")
    private boolean leader = false;

    @Builder
    private AccountClub(Account account, Club club) {
        this.account = account;
        this.club = club;
    }

    public static AccountClub of(Account account, Club club){
        return AccountClub.builder()
                .account(account)
                .club(club)
                .build();
    }

    public void addClub(Club club) {
        if(club.getParticipants() == 1){
            leader = true;
        }
        club.getAccountClubs().add(this);
    }

}
