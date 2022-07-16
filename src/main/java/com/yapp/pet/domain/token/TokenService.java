package com.yapp.pet.domain.token;

import com.yapp.pet.domain.account.entity.Account;
import com.yapp.pet.domain.token.entity.Token;
import com.yapp.pet.domain.token.repository.TokenRepository;
import com.yapp.pet.global.exception.jwt.NotFoundTokenException;
import com.yapp.pet.global.exception.jwt.NotRefreshTokenException;
import com.yapp.pet.global.jwt.JwtService;
import com.yapp.pet.global.jwt.TokenType;
import com.yapp.pet.web.token.model.TokenResponse;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TokenService {

    private final JwtService jwtService;

    private final TokenRepository tokenRepository;

    public TokenResponse reIssuance(Account account){
        TokenResponse tokenResponse = new TokenResponse();

        Token token = getAndValidToken(account);
        String uniqueIdBySocial = jwtService.getSubject(token.getRefreshToken());

        String createAccessToken = jwtService.createAccessToken(uniqueIdBySocial);
        String createRefreshToken = jwtService.createRefreshToken(uniqueIdBySocial);

        token.exchangeRefreshToken(createRefreshToken);

        tokenResponse.addToken(createAccessToken, createRefreshToken);

        return tokenResponse;
    }

    public Long expireRefreshToken(Account account){
        Token token = getAndValidToken(account);

        tokenRepository.delete(token);
        account.deleteToken();

        token.getId();
    }

    private Token getAndValidToken(Account account){
        if (account.getToken() == null) {
            throw new NotFoundTokenException();
        }

        String refreshToken = account.getToken().getRefreshToken();
        Claims claims = jwtService.parseClaims(refreshToken);

        if(!TokenType.isRefreshToken(claims.getAudience())){
            throw new NotRefreshTokenException();
        }

        return account.getToken();
    }

}
