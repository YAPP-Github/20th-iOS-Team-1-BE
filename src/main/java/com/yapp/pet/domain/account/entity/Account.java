package com.yapp.pet.domain.account.entity;

import com.yapp.pet.domain.account_image.AccountImage;
import com.yapp.pet.domain.common.BaseEntity;
import com.yapp.pet.domain.common.Category;
import com.yapp.pet.domain.token.entity.Token;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

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

    @Column(unique = true)
    private String email;

    @Column(nullable = false, length = 3)
    private Integer age;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountSex sex;

    @Embedded
    private Address address;

    @Column(length = 60)
    private String selfIntroduction;

    @Builder
    public Account(Long id, Token token, AccountImage accountImage, Set<Category> interestCategories, String nickname,
                   String email, Integer age, AccountSex sex, Address address, String selfIntroduction) {
        this.token = token;
        this.accountImage = accountImage;
        this.interestCategories = interestCategories;
        this.nickname = nickname;
        this.email = email;
        this.age = age;
        this.sex = sex;
        this.address = address;
        this.selfIntroduction = selfIntroduction;
    }

    public static Account of(Token token, String email) {
        String temporaryNickname = UUID.randomUUID().toString().substring(0, 8);

        return Account.builder()
                .token(token)
                .nickname(temporaryNickname)
                .email(email)
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

    public void update(Account updateAccount){
        if(StringUtils.hasText(updateAccount.getNickname())){
            this.nickname = updateAccount.getNickname();
        }

        if (updateAccount.getAge() != null) {
            this.age = updateAccount.getAge();
        }

        if (updateAccount.getSex() != null) {
            this.sex = updateAccount.getSex();
        }

        if (updateAccount.getAddress().getCity() != null) {
            this.address.updateCity(updateAccount.getAddress().getCity());
        }

        if (updateAccount.getAddress().getDetail() != null) {
            this.address.updateDetail(updateAccount.getAddress().getDetail());
        }

        if (StringUtils.hasText(updateAccount.getSelfIntroduction())) {
            this.selfIntroduction = updateAccount.getSelfIntroduction();
        }

        if (updateAccount.getInterestCategories() != null && updateAccount.getInterestCategories().size() != 0) {
            this.interestCategories.clear();
            this.interestCategories.addAll(updateAccount.interestCategories);
        }
    }

    public boolean isMe(Account account){
        return this.nickname.equals(account.getNickname());
    }

}
