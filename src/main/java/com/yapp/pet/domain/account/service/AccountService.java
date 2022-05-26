package com.yapp.pet.domain.account.service;

import com.yapp.pet.domain.account.AccountRepository;
import com.yapp.pet.domain.account.entity.Account;
import com.yapp.pet.domain.token.entity.Social;
import com.yapp.pet.domain.token.entity.Token;
import com.yapp.pet.domain.token.repository.TokenRepository;
import com.yapp.pet.global.jwt.JwtService;
import com.yapp.pet.web.oauth.apple.model.TokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    private final JwtService jwtService;

    private final AccountRepository accountRepository;
    private final TokenRepository tokenRepository;

    @Transactional
    public TokenResponse signIn(String idToken, Social social) {
        TokenResponse tokenResponse = new TokenResponse();

        String uniqueIdBySocial = jwtService.getSubject(idToken);

        String accessToken = jwtService.createAccessToken(uniqueIdBySocial);
        String refreshToken = jwtService.createRefreshToken(uniqueIdBySocial);
        tokenResponse.addToken(accessToken, refreshToken);

        Optional<Token> findToken = tokenRepository.findByUniqueIdBySocial(uniqueIdBySocial);

        findToken.ifPresentOrElse(token -> {
            log.info("social signIn - " + social.getValue());

            tokenResponse.setIsFirstAccount(Boolean.FALSE);
            token.exchangeRefreshToken(refreshToken);
        }, () -> {
            log.info("social signUp - " + social.getValue());

            tokenResponse.setIsFirstAccount(Boolean.TRUE);

            Token createToken = Token.of(uniqueIdBySocial, social, refreshToken);
            tokenRepository.save(createToken);

            Account createAccount = Account.of(createToken);
            accountRepository.save(createAccount);
        });

        return tokenResponse;
    }

}
