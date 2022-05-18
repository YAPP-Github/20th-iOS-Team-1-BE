package com.yapp.pet.domain.account_tag.entity;


import com.yapp.pet.domain.account.entity.Account;
import com.yapp.pet.domain.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "account_prefer_tag")
public class AccountTag extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_tag_id")
    private Long id;

    @Column(nullable = false, length = 10)
    private String name;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    private AccountTag(String name, Account account) {
        this.name = name;
        this.account = account;
    }

    public static AccountTag of(String name, Account account){
        return new AccountTag(name, account);
    }
}
