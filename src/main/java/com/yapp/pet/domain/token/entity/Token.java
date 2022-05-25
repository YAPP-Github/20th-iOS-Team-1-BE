package com.yapp.pet.domain.token.entity;

import com.yapp.pet.domain.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Token extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String uniqueIdentifier;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Social socialType;

    private String refreshToken;

    @Builder
    public Token(String uniqueIdentifier, Social socialType, String refreshToken) {
        this.uniqueIdentifier = uniqueIdentifier;
        this.socialType = socialType;
        this.refreshToken = refreshToken;
    }

    public static Token of(String uniqueIdentifier, Social social, String refreshToken){
        return Token.builder()
                .uniqueIdentifier(uniqueIdentifier)
                .socialType(social)
                .refreshToken(refreshToken)
                .build();
    }

    public void exchangeRefreshToken(String refreshToken){
        this.refreshToken = refreshToken;
    }

}
