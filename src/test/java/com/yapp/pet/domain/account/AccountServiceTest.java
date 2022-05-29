package com.yapp.pet.domain.account;

import com.yapp.pet.domain.token.entity.Social;
import com.yapp.pet.domain.token.entity.Token;
import com.yapp.pet.domain.token.repository.TokenRepository;
import com.yapp.pet.global.jwt.JwtService;
import com.yapp.pet.web.oauth.apple.model.SignInResponse;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;
import java.util.Date;

import static com.yapp.pet.global.TogaetherConstants.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Disabled
public class AccountServiceTest {

    @Autowired
    AccountService accountService;

    @Autowired
    JwtService jwtService;

    @Autowired
    TokenRepository tokenRepository;

    @Value("${jwt.token.secret}")
    String secret;

    String idToken;
    final String UNIQUE_ID = "uniqueIdBySocial";

    @BeforeEach
    void init(){
        long now = (new Date()).getTime();
        Date expiration = new Date(now + 1800000);

        Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));

        idToken = Jwts.builder()
                .signWith(key, SignatureAlgorithm.HS512)
                .setHeaderParam(JWT_HEADER_PARAM_TYPE, "JWT")
                .setIssuer("APPLE")
                .setSubject(UNIQUE_ID)
                .setExpiration(expiration)
                .setIssuedAt(new Date())
                .claim(AUTHORITIES_KEY, ROLE)
                .compact();
    }

    @Test
    @Transactional
    void 회원가입(){
        //when
        SignInResponse signInResponse = accountService.signIn(idToken, Social.APPLE);

        //then
        assertThat(signInResponse.getFirstAccount()).isTrue();
        assertThat(signInResponse.getAccessToken()).isNotNull().isNotEmpty();
        assertThat(signInResponse.getRefreshToken()).isNotNull().isNotEmpty();
    }

    @Test
    void 로그인(){
        //given
        String refreshToken = jwtService.createRefreshToken(UNIQUE_ID);

        Token token = Token.builder()
                .uniqueIdBySocial(UNIQUE_ID)
                .refreshToken(refreshToken)
                .socialType(Social.APPLE)
                .build();

        tokenRepository.save(token);

        //when
        SignInResponse signInResponse = accountService.signIn(idToken, Social.APPLE);

        //then
        assertThat(signInResponse.getFirstAccount()).isFalse();
        assertThat(signInResponse.getAccessToken()).isNotNull().isNotEmpty();
        assertThat(signInResponse.getRefreshToken()).isNotNull().isNotEmpty();
    }

}
