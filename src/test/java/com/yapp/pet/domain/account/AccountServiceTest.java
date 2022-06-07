package com.yapp.pet.domain.account;

import com.yapp.pet.domain.account.entity.Account;
import com.yapp.pet.domain.account.repository.AccountRepository;
import com.yapp.pet.domain.token.entity.Social;
import com.yapp.pet.domain.token.repository.TokenRepository;
import com.yapp.pet.global.jwt.JwtService;
import com.yapp.pet.global.jwt.TokenType;
import com.yapp.pet.web.account.model.AccountValidationResponse;
import com.yapp.pet.web.oauth.apple.model.SignInResponse;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;
import java.util.Date;

import static com.yapp.pet.global.TogaetherConstants.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Sql({"/data.sql"})
public class AccountServiceTest {

    @Autowired
    AccountService accountService;

    @Autowired
    JwtService jwtService;

    @Autowired
    TokenRepository tokenRepository;

    @Autowired
    AccountRepository accountRepository;

    @Value("${jwt.token.secret}")
    String secret;

    String createIdToken(String uniqueId){
        long now = (new Date()).getTime();
        Date expiration = new Date(now + 1800000);

        Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));

        return Jwts.builder()
                .signWith(key, SignatureAlgorithm.HS512)
                .setHeaderParam(JWT_HEADER_PARAM_TYPE, "JWT")
                .setIssuer("yapp")
                .setSubject(uniqueId)
                .setAudience(TokenType.ACCESS.toString())
                .setExpiration(expiration)
                .setIssuedAt(new Date())
                .claim(AUTHORITIES_KEY, ROLE)
                .compact();
    }

    @Test
    @DisplayName("회원가입할 수 있다.")
    void SignUp(){
        //given
        String idToken = createIdToken("newUniqueId");

        //when
        SignInResponse signInResponse = accountService.signIn(idToken, Social.APPLE);

        //then
        assertThat(signInResponse.getFirstAccount()).isTrue();
        assertThat(signInResponse.getAccessToken()).isNotNull().isNotEmpty();
        assertThat(signInResponse.getRefreshToken()).isNotNull().isNotEmpty();
    }

    @Test
    @DisplayName("로그인할 수 있다.")
    void SignIn(){
        //given
        Account account = accountRepository.findById(1L).get();
        String refreshToken = account.getToken().getRefreshToken();

        //when
        SignInResponse signInResponse = accountService.signIn(refreshToken, Social.APPLE);

        //then
        assertThat(signInResponse.getFirstAccount()).isFalse();
        assertThat(signInResponse.getAccessToken()).isNotNull().isNotEmpty();
        assertThat(signInResponse.getRefreshToken()).isNotNull().isNotEmpty();
        assertThat(signInResponse.getRefreshToken()).isNotEqualTo(refreshToken);
    }

    @Test
    @DisplayName("[성공] 닉네임 검증 로직 통과 - 중복되지 않고 2글자~10글자인 닉네임")
    void successValidateNickname(){
        //given
        String nickname = "투개더";

        //when
        AccountValidationResponse response = accountService.validateNickname(nickname);

        //then
        assertThat(response.isSatisfyLengthCondition()).isTrue();
        assertThat(response.isUnique()).isTrue();
    }

    @Test
    @DisplayName("닉네임 검증 - 닉네임이 중복되는 경우")
    void duplicateNickname(){
        //given
        String nickname = "재롱잔치";

        //when
        AccountValidationResponse response = accountService.validateNickname(nickname);

        //then
        assertThat(response.isSatisfyLengthCondition()).isTrue();
        assertThat(response.isUnique()).isFalse();
    }

    @Test
    @DisplayName("닉네임 검증 - 닉네임의 길이는 2글자~10글자가 아닌 경우")
    void notSatisfyLengthConditionNickname(){
        //given
        String nickname = "yapp-togaether";

        //when
        AccountValidationResponse response = accountService.validateNickname(nickname);

        //then
        assertThat(response.isSatisfyLengthCondition()).isFalse();
        assertThat(response.isUnique()).isTrue();
    }

}
