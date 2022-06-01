package com.yapp.pet.domain.token;

import com.yapp.pet.domain.token.entity.Social;
import com.yapp.pet.domain.token.entity.Token;
import com.yapp.pet.domain.token.repository.TokenRepository;
import com.yapp.pet.global.exception.common.ExceptionStatus;
import com.yapp.pet.global.exception.jwt.InvalidJwtTokenException;
import com.yapp.pet.global.exception.jwt.NotRefreshTokenException;
import com.yapp.pet.global.jwt.JwtService;
import com.yapp.pet.global.jwt.TokenType;
import com.yapp.pet.web.token.model.TokenResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.NoSuchElementException;

import static com.yapp.pet.global.TogaetherConstants.AUTHORIZATION_HEADER_BEARER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TokenServiceTest {

    @Autowired
    TokenService tokenService;

    @Autowired
    JwtService jwtService;

    @Autowired
    TokenRepository tokenRepository;

    @Mock
    HttpServletRequest mockedRequest;

    final String UNIQUE_ID = "uniqueIdBySocial";
    String refreshToken;

    @BeforeEach
    void init(){
        refreshToken = jwtService.createRefreshToken(UNIQUE_ID);

        Token token = Token.builder()
                .uniqueIdBySocial(UNIQUE_ID)
                .refreshToken(refreshToken)
                .socialType(Social.APPLE)
                .build();

        tokenRepository.save(token);
    }

    @Test
    @Transactional
    @DisplayName("[성공] refresh token이 DB에 존재하면 access token과 refresh token을 재발급한다")
    void reIssuanceToken() throws InterruptedException {
        //given
        when(mockedRequest.getHeader("Authorization"))
                .thenReturn(AUTHORIZATION_HEADER_BEARER + refreshToken);

        // refreshToken 생성 직후, refreshToken을 다시 생성하면, 같은 refreshToken 값이 나오는걸 방지(Date로 인해)
        // 실제로 이런 케이스는 없으므로, 테스트에서는 sleep으로 처리
        Thread.sleep(2000L);

        //when
        TokenResponse tokenResponse = tokenService.reIssuance(mockedRequest);
        String reAccessToken = tokenResponse.getAccessToken();
        String reRefreshToken = tokenResponse.getRefreshToken();
        Token findToken = tokenRepository.findByUniqueIdBySocial(UNIQUE_ID).get();

        //then
        assertThat(reAccessToken).isNotNull().isNotEmpty();
        assertThat(jwtService.parseClaims(reAccessToken).getAudience())
                .isEqualTo(TokenType.ACCESS.toString());

        assertThat(reRefreshToken).isNotEqualTo(refreshToken);
        assertThat(jwtService.parseClaims(reRefreshToken).getAudience())
                .isEqualTo(TokenType.REFRESH.toString());

        assertThat(findToken.getRefreshToken()).isEqualTo(reRefreshToken);
    }

    @Test
    @Transactional
    @DisplayName("[실패] refresh token이 DB에 존재하지 않으면 예외가 발생한다")
    void failureReIssuanceTokenByNotFountRefreshToken(){
        //given
        String refreshToken = jwtService.createRefreshToken("anything");
        when(mockedRequest.getHeader("Authorization"))
                .thenReturn(AUTHORIZATION_HEADER_BEARER + refreshToken);

        //when & then
        assertThatExceptionOfType(InvalidJwtTokenException.class)
                .isThrownBy(() -> tokenService.reIssuance(mockedRequest))
                .withMessageMatching(ExceptionStatus.INVALID_JWT_TOKEN_EXCEPTION.getMessage());
    }

    @Test
    @Transactional
    @DisplayName("[실패] 토큰_재발급할때_refersh_token이_아니면_예외가_발생한다")
    void failureReIssuanceTokenByNotRefreshToken(){
        //given
        String accessToken = jwtService.createAccessToken("anything");
        when(mockedRequest.getHeader("Authorization"))
                .thenReturn(AUTHORIZATION_HEADER_BEARER + accessToken);

        //when & then
        assertThatExceptionOfType(NotRefreshTokenException.class)
                .isThrownBy(() -> tokenService.reIssuance(mockedRequest))
                .withMessageMatching(ExceptionStatus.NOT_REFRESH_TOKEN.getMessage());
    }

    @Test
    @Transactional
    @DisplayName("[성공]존재하는 refresh token을 삭제한다")
    void deleteRefreshToken(){
        //given
        when(mockedRequest.getHeader("Authorization"))
                .thenReturn(AUTHORIZATION_HEADER_BEARER + refreshToken);

        //when
        tokenService.expireRefreshToken(mockedRequest);

        //then
        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(() -> tokenRepository.findByUniqueIdBySocial(UNIQUE_ID).get());
    }

    @Test
    @Transactional
    @DisplayName("[실패]존재하지 않는 refresh token 삭제시 예외가 발생한다")
    void failureDeleteRefreshTokenByNotFoundRefreshToken(){
        //given
        String refreshToken = jwtService.createRefreshToken("anything");
        when(mockedRequest.getHeader("Authorization"))
                .thenReturn(AUTHORIZATION_HEADER_BEARER + refreshToken);

        //when & then
        assertThatExceptionOfType(InvalidJwtTokenException.class)
                .isThrownBy(() -> tokenService.expireRefreshToken(mockedRequest));
    }

}
