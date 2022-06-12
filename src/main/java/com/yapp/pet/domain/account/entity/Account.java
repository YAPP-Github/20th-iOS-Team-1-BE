package com.yapp.pet.domain.account.entity;

import com.yapp.pet.domain.account_image.AccountImage;
import com.yapp.pet.domain.common.BaseEntity;
import com.yapp.pet.domain.common.Category;
import com.yapp.pet.domain.token.entity.Token;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long id;

    @OneToOne(fetch = LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "token_id")
    private Token token;

    @OneToOne(fetch = LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "account_image_id")
    private AccountImage accountImage;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "interest_categories", joinColumns = @JoinColumn(name = "account_id"))
    private Set<Category> interestCategories = new HashSet<>();

    @Column(nullable = false, length = 10, unique = true)
    private String nickname;

    @Column(nullable = false, length = 3)
    private Integer age;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountSex sex;

    @Embedded
    private Address address;

    @Column(length = 200)
    private String selfIntroduction;

    @Builder
    public Account(Long id, Token token, AccountImage accountImage, Set<Category> interestCategories,
                   String nickname, Integer age, AccountSex sex, Address address, String selfIntroduction) {
        this.token = token;
        this.accountImage = accountImage;
        this.interestCategories = interestCategories;
        this.nickname = nickname;
        this.age = age;
        this.sex = sex;
        this.address = address;
        this.selfIntroduction = selfIntroduction;
    }

    public static Account of(Token token) {
        String temporaryNickname = UUID.randomUUID().toString().substring(0, 8);

        return Account.builder()
                .token(token)
                .nickname(temporaryNickname)
                .age(0)
                .sex(AccountSex.PRIVATE)
                .build();
    }

    public void signUp(Account account){
        this.nickname = account.getNickname();
        this.age = account.getAge();
        this.sex = account.getSex();
        this.address = account.getAddress();
    }

    public void deleteToken(){
        this.token = null;
    }

    public void addImage(AccountImage accountImage){
        this.accountImage = accountImage;
    }

}
