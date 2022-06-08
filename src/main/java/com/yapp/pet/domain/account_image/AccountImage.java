package com.yapp.pet.domain.account_image;

import com.yapp.pet.domain.account.entity.Account;
import com.yapp.pet.domain.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccountImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_image_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @Column(nullable = false)
    private String originName;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String path;

    @Builder
    public AccountImage(Account account, String originName, String name, String path) {
        this.account = account;
        this.originName = originName;
        this.name = name;
        this.path = path;
    }

}
