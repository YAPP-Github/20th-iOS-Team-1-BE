package com.yapp.pet.global.jwt;

import com.yapp.pet.global.exception.common.ExceptionStatus;
import com.yapp.pet.global.exception.jwt.ExpiredJwtTokenException;
import com.yapp.pet.global.exception.jwt.InvalidJwtTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import io.jsonwebtoken.security.WeakKeyException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.Key;
import java.util.Date;

import static com.yapp.pet.global.TogaetherConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
public class JwtServiceTest {

    @Autowired
    JwtService jwtService;

    @Value("${jwt.token.secret}")
    String secret;

    String accessToken;
    String refreshToken;
    final String UNIQUE_ID = "uniqueIdBySocial";

    @BeforeEach
    void init(){
        accessToken = jwtService.createAccessToken(UNIQUE_ID);
        refreshToken = jwtService.createRefreshToken(UNIQUE_ID);
    }

    String createTestAccessToken(String secret, Date expiration){
        Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));

        return Jwts.builder()
                .signWith(key, SignatureAlgorithm.HS512)
                .setHeaderParam(JWT_HEADER_PARAM_TYPE, "JWT")
                .setIssuer("yapp")
                .setSubject(UNIQUE_ID)
                .setAudience(TokenType.ACCESS.toString())
                .setExpiration(expiration)
                .setIssuedAt(new Date())
                .claim(AUTHORITIES_KEY, ROLE)
                .compact();
    }

    @Test
    @DisplayName("[??????] ????????????_sub_?????????_?????????.")
    void getSubFromToken(){
        //when
        String sub = jwtService.getSubject(accessToken);

        //then
        assertThat(sub).isEqualTo(UNIQUE_ID);
    }

    @Test
    @DisplayName("[??????] ???????????? sub??? ???????????? ????????? ????????? ????????? ????????????.")
    void failureGetSubFromTokenByExpired(){
        //when
        String accessToken = createTestAccessToken(this.secret, new Date(1));

        //then
        assertThatExceptionOfType(ExpiredJwtTokenException.class)
                .isThrownBy(() -> jwtService.getSubject(accessToken))
                .withMessageMatching(ExceptionStatus.EXPIRED_JWT_TOKEN_EXCEPTION.getMessage());
    }

    @Test
    @DisplayName("[??????] ???????????? sub??? ???????????? secret??? ????????? ????????? ????????????.")
    void failureGetSubFromTokenByNotEqualsSecret(){
        //given
        String secret = "sdjiocdjapjcicjsiod294hf7hf78sc687gs6" +
                "dcg6dsg6c8gdcauysldj1ly3g78gta7sdgjk12hbe7uhd7hsufhdjsfh3729h729";

        //when
        String accessToken = createTestAccessToken(secret, new Date(999999999));

        //then
        assertThatExceptionOfType(SignatureException.class)
                .isThrownBy(() -> jwtService.getSubject(accessToken));
    }

    @Test
    @DisplayName("[??????] access token??? ????????????")
    void createAccessToken(){
        //when
        String accessToken = jwtService.createAccessToken(UNIQUE_ID);
        String sub = jwtService.getSubject(accessToken);
        Claims claims = jwtService.parseClaims(accessToken);

        //then
        assertThat(accessToken).isNotNull().isNotEmpty();
        assertThat(sub).isEqualTo(UNIQUE_ID);
        assertThat(claims.getAudience()).isEqualTo(TokenType.ACCESS.toString());
    }

    @Test
    @DisplayName("[??????] access_token??? ????????? secret ????????? ????????? ????????? ????????????.")
    void failureGetSubFromTokenByTooShorSecret(){
        //given
        String secret = "acxvikobvjoif2s";

        //when & then
        assertThatExceptionOfType(WeakKeyException.class)
                .isThrownBy(() -> createTestAccessToken(secret, new Date(999999999)));
    }

    @Test
    @DisplayName("[??????] refresh token??? ????????????")
    void createRefreshToken(){
        //when
        String refreshToken = jwtService.createRefreshToken(UNIQUE_ID);
        String sub = jwtService.getSubject(refreshToken);
        Claims claims = jwtService.parseClaims(refreshToken);

        //then
        assertThat(refreshToken).isNotNull().isNotEmpty();
        assertThat(sub).isEqualTo(UNIQUE_ID);
        assertThat(claims.getAudience()).isEqualTo(TokenType.REFRESH.toString());
    }

    @Test
    @DisplayName("[??????] ????????? ???????????? ????????? ????????? ????????????.")
    void notExistToken(){

        //when & then
        assertThatExceptionOfType(InvalidJwtTokenException.class)
                .isThrownBy(() -> jwtService.validateToken(null))
                .withMessageMatching(ExceptionStatus.INVALID_JWT_TOKEN_EXCEPTION.getMessage());

        assertThatExceptionOfType(InvalidJwtTokenException.class)
                .isThrownBy(() -> jwtService.validateToken(""))
                .withMessageMatching(ExceptionStatus.INVALID_JWT_TOKEN_EXCEPTION.getMessage());
    }

}
