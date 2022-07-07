package com.yapp.pet.domain.account.service;

import com.yapp.pet.domain.account.entity.Account;
import com.yapp.pet.domain.account.repository.AccountRepository;
import com.yapp.pet.domain.account_image.AccountImage;
import com.yapp.pet.domain.account_image.AccountImageService;
import com.yapp.pet.domain.token.entity.Social;
import com.yapp.pet.domain.token.entity.Token;
import com.yapp.pet.domain.token.repository.TokenRepository;
import com.yapp.pet.global.jwt.JwtService;
import com.yapp.pet.global.mapper.AccountMapper;
import com.yapp.pet.web.account.model.AccountSignUpRequest;
import com.yapp.pet.web.account.model.AccountUpdateRequest;
import com.yapp.pet.web.oauth.apple.model.SignInResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AccountService {

    private final JwtService jwtService;
    private final AccountImageService accountImageService;

    private final AccountMapper accountMapper;

    private final AccountRepository accountRepository;
    private final TokenRepository tokenRepository;

    public SignInResponse signIn(String idToken, Social social) {
        SignInResponse signInResponse = new SignInResponse();

        String uniqueIdBySocial;

        if (social.equals(Social.APPLE)) {
            uniqueIdBySocial = jwtService.getSubjectByAppleToken(idToken);
        } else {
            uniqueIdBySocial = jwtService.getSubject(idToken);
        }

        String createAccessToken = jwtService.createAccessToken(uniqueIdBySocial);
        String createRefreshToken = jwtService.createRefreshToken(uniqueIdBySocial);
        signInResponse.addToken(createAccessToken, createRefreshToken);

        Optional<Token> findRefreshToken = tokenRepository.findByUniqueIdBySocial(uniqueIdBySocial);

        findRefreshToken.ifPresentOrElse(token -> {
            log.info("social signIn - " + social.getValue());

            signInResponse.setFirstAccount(FALSE);
            token.exchangeRefreshToken(createRefreshToken);
        }, () -> {
            log.info("social signUp - " + social.getValue());

            signInResponse.setFirstAccount(TRUE);

            Token createToken = Token.of(uniqueIdBySocial, social, createRefreshToken);
            tokenRepository.save(createToken);

            Account createAccount = Account.of(createToken);
            accountRepository.save(createAccount);

            createToken.addAccount(createAccount);
        });

        return signInResponse;
    }

    public Long signUp(Account account, AccountSignUpRequest signUpRequest, MultipartFile imageFile) {

        Account updateAccount = accountMapper.toEntity(signUpRequest);

        account.signUp(updateAccount);

        if (hasImageFile(imageFile)) {
            AccountImage accountImage = accountImageService.create(imageFile);
            account.addImage(accountImage);
        }

        return account.getId();
    }

    public void updateAccount(Account account, AccountUpdateRequest request) {
        AccountImage accountImage = account.getAccountImage();
        MultipartFile imageFile = request.getImageFile();

        if (hasImageFile(imageFile)) {
            if (accountImage != null) {
                accountImageService.delete(account);
            }

            AccountImage createAccountImage = accountImageService.create(imageFile);
            account.addImage(createAccountImage);
        }

        Account updateAccount = accountMapper.toEntity(request);

        account.update(updateAccount);
    }

    private boolean hasImageFile(MultipartFile imageFile){
        return imageFile != null && !imageFile.isEmpty();
    }

}
