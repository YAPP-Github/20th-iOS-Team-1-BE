package com.yapp.pet.domain.token;

import com.yapp.pet.domain.account.entity.Account;
import com.yapp.pet.domain.account.entity.AccountSex;
import com.yapp.pet.domain.account.repository.AccountRepository;
import com.yapp.pet.domain.token.entity.Social;
import com.yapp.pet.domain.token.entity.Token;
import com.yapp.pet.domain.token.repository.TokenRepository;
import com.yapp.pet.global.exception.common.ExceptionStatus;
import com.yapp.pet.global.exception.jwt.NotFoundTokenException;
import com.yapp.pet.global.exception.jwt.NotRefreshTokenException;
import com.yapp.pet.global.jwt.JwtService;
import com.yapp.pet.global.jwt.TokenType;
import com.yapp.pet.web.token.model.TokenResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
@Transactional
@Sql({"/data.sql"})
public class TokenServiceTest {

    @Autowired
    TokenService tokenService;

    @Autowired
    JwtService jwtService;

    @Autowired
    TokenRepository tokenRepository;

    @Autowired
    AccountRepository accountRepository;

    final String UNIQUE_ID = "unique1";

    Account accountWithToken;
    Account accountWithoutToken;

    @BeforeEach
    void init(){
        accountWithToken = accountRepository.findById(1L).get();

        accountWithoutToken = Account.builder()
                .age(10)
                .sex(AccountSex.MAN)
                .nickname("test")
                .build();
    }

    @Test
    @DisplayName("[성공] refresh token이 DB에 존재하면 access token과 refresh token을 재발급한다")
    void reIssuanceToken() {

        //given
        String beforeRefreshToken = accountWithToken.getToken().getRefreshToken();

        //when
        TokenResponse tokenResponse = tokenService.reIssuance(accountWithToken);
        String reAccessToken = tokenResponse.getAccessToken();
        String reRefreshToken = tokenResponse.getRefreshToken();
        Token findToken = tokenRepository.findByUniqueIdBySocial(UNIQUE_ID).get();

        //then
        assertThat(reAccessToken).isNotNull().isNotEmpty();
        assertThat(jwtService.parseClaims(reAccessToken).getAudience())
                .isEqualTo(TokenType.ACCESS.toString());

        assertThat(reRefreshToken).isNotEqualTo(beforeRefreshToken);
        assertThat(jwtService.parseClaims(reRefreshToken).getAudience())
                .isEqualTo(TokenType.REFRESH.toString());

        assertThat(findToken.getRefreshToken()).isEqualTo(reRefreshToken);
    }

    @Test
    @DisplayName("[예외] refresh token이 DB에 존재하지 않으면 예외가 발생한다")
    void failureReIssuanceTokenByNotFountRefreshToken(){

        //when & then
        assertThatExceptionOfType(NotFoundTokenException.class)
                .isThrownBy(() -> tokenService.reIssuance(accountWithoutToken))
                .withMessageMatching(ExceptionStatus.NOT_FOUND_TOKEN_EXCEPTION.getMessage());
    }

    @Test
    @DisplayName("[예외] 토큰 재발급시 refersh token이 아니면 예외가 발생한다")
    void failureReIssuanceTokenByNotRefreshToken(){

        //given
        String accessToken = jwtService.createAccessToken("test");

        Token token = Token.builder()
                .socialType(Social.APPLE)
                .uniqueIdBySocial("test")
                .refreshToken(accessToken)
                .build();

        Account account = Account.of(token, "yapp@email.com");

        //when & then
        assertThatExceptionOfType(NotRefreshTokenException.class)
                .isThrownBy(() -> tokenService.reIssuance(account))
                .withMessageMatching(ExceptionStatus.NOT_REFRESH_TOKEN_EXCEPTION.getMessage());
    }

    @Test
    @DisplayName("[성공] 존재하는 refresh token을 삭제한다")
    void deleteRefreshToken(){

        //when
        tokenService.expireRefreshToken(accountWithToken);

        //then
        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(() -> tokenRepository.findByUniqueIdBySocial(UNIQUE_ID).get());
    }

    @Test
    @DisplayName("[예외] 존재하지 않는 refresh token 삭제시 예외가 발생한다")
    void failureDeleteRefreshTokenByNotFoundRefreshToken(){

        //when & then
        assertThatExceptionOfType(NotFoundTokenException.class)
                .isThrownBy(() -> tokenService.expireRefreshToken(accountWithoutToken))
                .withMessageMatching(ExceptionStatus.NOT_FOUND_TOKEN_EXCEPTION.getMessage());
    }

}
