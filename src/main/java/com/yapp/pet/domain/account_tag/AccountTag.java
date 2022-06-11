package com.yapp.pet.domain.account_tag;

import com.yapp.pet.domain.account.entity.Account;
import com.yapp.pet.domain.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccountTag extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_tag_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @Column(nullable = false, length = 10)
    private String name;

    private AccountTag(Account account, String name) {
        this.account = account;
        this.name = name;
    }

    public static AccountTag of(Account account, String name){
        return new AccountTag(account, name);
    }
}
