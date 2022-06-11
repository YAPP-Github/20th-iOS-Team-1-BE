package com.yapp.pet.domain.account_tag;

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

    @Column(nullable = false, length = 10)
    private String name;

    private AccountTag(String name) {
        this.name = name;
    }

    public static AccountTag of(String name){
        return new AccountTag(name);
    }
}
