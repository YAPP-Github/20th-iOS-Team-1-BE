package com.yapp.pet.domain.token;

import com.yapp.pet.domain.token.entity.Token;
import com.yapp.pet.domain.token.repository.TokenRepository;
import com.yapp.pet.global.exception.jwt.InvalidJwtTokenException;
import com.yapp.pet.global.jwt.JwtAuthentication;
import com.yapp.pet.global.jwt.JwtService;
import com.yapp.pet.web.oauth.apple.model.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        final String bearerToken = httpRequest.getHeader(AUTHORIZATION_HEADER);
        String jwt = bearerToken.substring(AUTHORIZATION_HEADER_BEARER.length());

        JwtAuthentication authentication = jwtService.getAuthentication(jwt);
        String uniqueIdentifier = String.valueOf(authentication.getPrincipal());

        Optional<Token> findToken = tokenRepository.findByUniqueIdentifier(uniqueIdentifier);

        findToken.ifPresentOrElse(token -> {
            String accessToken = jwtService.createAccessToken(authentication);
            String refreshToken = jwtService.createRefreshToken(authentication);

            token.exchangeRefreshToken(refreshToken);

            tokenResponse.addToken(accessToken, refreshToken);
        }, () -> {
            throw new InvalidJwtTokenException();
        });

        return tokenResponse;
    }

}
