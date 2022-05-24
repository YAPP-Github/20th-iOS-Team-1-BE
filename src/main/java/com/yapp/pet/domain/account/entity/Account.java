package com.yapp.pet.domain.account.entity;

import com.yapp.pet.domain.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long id;

    @Column(nullable = false, length = 50)
    private String loginId;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false, length = 20, unique = true)
    private String nickname;

    @Column(nullable = false, length = 3)
    private int age;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountSex sex;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String refreshToken;

    @Builder
    public Account(String loginId, String password, String nickname,
                   int age, AccountSex sex, Address address, Role role) {
        this.loginId = loginId;
        this.password = password;
        this.nickname = nickname;
        this.age = age;
        this.sex = sex;
        this.address = address;
        this.role = role;
    }

    public void addRefreshToken(String refreshToken){
        this.refreshToken = refreshToken;
    }

    public void expireRefreshToken(){
        this.refreshToken = null;
    }
}
