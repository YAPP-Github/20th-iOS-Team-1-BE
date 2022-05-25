package com.yapp.pet.domain.account.entity;

import com.yapp.pet.domain.common.BaseEntity;
import com.yapp.pet.domain.token.entity.Token;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long id;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "token_id")
    private Token token;

    @Column(nullable = false, length = 20, unique = true)
    private String nickname;

    @Column(nullable = false, length = 3)
    private int age;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountSex sex;

    @Embedded
    private Address address;

    @Builder
    public Account(Token token, String nickname, int age, AccountSex sex, Address address) {
        this.token = token;
        this.nickname = nickname;
        this.age = age;
        this.sex = sex;
        this.address = address;
    }

    public static Account of(Token token) {
        return Account.builder()
                .token(token)
                .build();
    }

}
