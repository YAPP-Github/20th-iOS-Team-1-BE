package com.yapp.pet.domain.token;

import com.yapp.pet.domain.token.entity.Token;
import com.yapp.pet.domain.token.repository.TokenRepository;
import com.yapp.pet.global.exception.jwt.InvalidJwtTokenException;
import com.yapp.pet.global.exception.jwt.NotRefreshTokenException;
import com.yapp.pet.global.jwt.JwtService;
import com.yapp.pet.global.jwt.TokenType;
import com.yapp.pet.web.token.model.TokenResponse;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

import static com.yapp.pet.global.TogaetherConstants.AUTHORIZATION_HEADER;
import static com.yapp.pet.global.TogaetherConstants.AUTHORIZATION_HEADER_BEARER;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtService jwtService;

    private final TokenRepository tokenRepository;

    @Transactional
    public TokenResponse reIssuance(HttpServletRequest httpRequest){
        TokenResponse tokenResponse = new TokenResponse();

        String requestRefreshToken = getAndValidRefreshToken(httpRequest);

        String uniqueIdBySocial = jwtService.getSubject(requestRefreshToken);

        Optional<Token> findRefreshToken = tokenRepository.findByUniqueIdBySocial(uniqueIdBySocial);

        findRefreshToken.ifPresentOrElse(token -> {
            String createAccessToken = jwtService.createAccessToken(uniqueIdBySocial);
            String createRefreshToken = jwtService.createRefreshToken(uniqueIdBySocial);

            token.exchangeRefreshToken(createRefreshToken);

            tokenResponse.addToken(createAccessToken, createRefreshToken);
        }, () -> {
            throw new InvalidJwtTokenException();
        });

        return tokenResponse;
    }

    @Transactional
    public void expireRefreshToken(HttpServletRequest httpRequest){
        String requestRefreshToken = getAndValidRefreshToken(httpRequest);

        String uniqueIdBySocial = jwtService.getSubject(requestRefreshToken);

        Optional<Token> findRefreshToken = tokenRepository.findByUniqueIdBySocial(uniqueIdBySocial);

        findRefreshToken.ifPresentOrElse(tokenRepository::delete, () -> {
            throw new InvalidJwtTokenException();
        });
    }

    private String getAndValidRefreshToken(HttpServletRequest httpRequest){
        final String bearerToken = httpRequest.getHeader(AUTHORIZATION_HEADER);

        if (!(StringUtils.hasText(bearerToken) && bearerToken.startsWith(AUTHORIZATION_HEADER_BEARER))) {
            throw new InvalidJwtTokenException();
        }

        String refreshToken = bearerToken.substring(AUTHORIZATION_HEADER_BEARER.length());
        Claims claims = jwtService.parseClaims(refreshToken);

        if(!TokenType.isRefreshToken(claims.getAudience())){
            throw new NotRefreshTokenException();
        }

        return refreshToken;
    }

}
