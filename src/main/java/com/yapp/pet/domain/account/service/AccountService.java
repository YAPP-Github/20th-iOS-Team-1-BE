package com.yapp.pet.domain.account.service;

import com.yapp.pet.domain.account.AccountRepository;
import com.yapp.pet.domain.account.entity.Account;
import com.yapp.pet.domain.token.entity.Social;
import com.yapp.pet.domain.token.entity.Token;
import com.yapp.pet.domain.token.repository.TokenRepository;
import com.yapp.pet.global.jwt.JwtAuthentication;
import com.yapp.pet.global.jwt.JwtService;
import com.yapp.pet.web.oauth.apple.model.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final JwtService jwtService;

    private final AccountRepository accountRepository;
    private final TokenRepository tokenRepository;

    @Transactional
    public TokenResponse signIn(String idToken, Social social) {
        TokenResponse tokenResponse = new TokenResponse();

        JwtAuthentication authentication = jwtService.getAuthentication(idToken);
        String uniqueIdentifier = String.valueOf(authentication.getPrincipal());

        String accessToken = jwtService.createAccessToken(authentication);
        String refreshToken = jwtService.createRefreshToken(authentication);
        tokenResponse.addToken(accessToken, refreshToken);

        Optional<Token> findToken = tokenRepository.findByUniqueIdentifier(uniqueIdentifier);

        findToken.ifPresentOrElse(token -> {
            tokenResponse.setFirstAccount(false);
            token.exchangeRefreshToken(refreshToken);
        }, () -> {
            tokenResponse.setFirstAccount(true);

            Token createToken = Token.of(uniqueIdentifier, social, refreshToken);
            tokenRepository.save(createToken);

            Account saveAccount = accountRepository.save(Account.of(createToken));
            tokenResponse.setAccountId(saveAccount.getId());
        });

        return tokenResponse;
    }

}
