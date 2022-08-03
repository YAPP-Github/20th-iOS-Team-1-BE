package com.yapp.pet.domain.account.service;

import com.yapp.pet.domain.account.event.SavedImageEvent;
import com.yapp.pet.domain.account.entity.Account;
import com.yapp.pet.domain.account.repository.AccountRepository;
import com.yapp.pet.domain.account_image.AccountImage;
import com.yapp.pet.domain.account_image.AccountImageService;
import com.yapp.pet.domain.accountclub.AccountClubRepository;
import com.yapp.pet.domain.comment.service.CommentService;
import com.yapp.pet.domain.pet.service.PetService;
import com.yapp.pet.domain.token.entity.Social;
import com.yapp.pet.domain.token.entity.Token;
import com.yapp.pet.domain.token.repository.TokenRepository;
import com.yapp.pet.global.jwt.JwtService;
import com.yapp.pet.global.mapper.AccountMapper;
import com.yapp.pet.web.account.model.AccountSignUpRequest;
import com.yapp.pet.web.account.model.AccountUpdateRequest;
import com.yapp.pet.web.oauth.apple.AppleClient;
import com.yapp.pet.web.oauth.apple.model.ApplePublicKeyResponse;
import com.yapp.pet.web.oauth.apple.model.AppleRequest;
import com.yapp.pet.web.oauth.apple.model.SignInResponse;
import com.yapp.pet.web.oauth.kakao.model.KakaoTokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
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

    private final AccountRepository accountRepository;
    private final TokenRepository tokenRepository;
    private final AccountMapper accountMapper;
    private final AppleClient appleClient;

    private final PetService petService;
    private final CommentService commentService;
    private final AccountClubRepository accountClubRepository;

    private final ApplicationEventPublisher eventPublisher;

    public SignInResponse signInFromApple(AppleRequest appleRequest, Social social) {
        ApplePublicKeyResponse response = appleClient.getApplePublicKey();

        String uniqueIdBySocial = jwtService.getSubjectByAppleToken(appleRequest.getIdToken(), response);

        return signIn(social, uniqueIdBySocial, appleRequest.getEmail());
    }

    public SignInResponse signInFromKakao(KakaoTokenResponse kakaoTokenResponse, Social social) {
        String uniqueIdBySocial = jwtService.getSubject(kakaoTokenResponse.getIdToken());

        return signIn(social, uniqueIdBySocial,  kakaoTokenResponse.getEmail());
    }

    public SignInResponse signIn(Social social, String uniqueIdBySocial, String email) {
        SignInResponse signInResponse = new SignInResponse();

        String createdAccessToken = jwtService.createAccessToken(uniqueIdBySocial);
        String createdRefreshToken = jwtService.createRefreshToken(uniqueIdBySocial);
        signInResponse.addToken(createdAccessToken, createdRefreshToken);

        Token createdToken = Token.of(uniqueIdBySocial, social, createdRefreshToken);
        tokenRepository.save(createdToken);

        Optional<Account> findAccount = accountRepository.findByEmail(email);

        findAccount.ifPresentOrElse(account -> {
            log.info("social signIn - " + social.getValue());

            signInResponse.setFirstAccount(FALSE);
            createdToken.addAccount(account);
        }, () -> {
            log.info("social signUp - " + social.getValue());

            signInResponse.setFirstAccount(TRUE);

            Account createAccount = Account.of(createdToken, email);
            accountRepository.save(createAccount);

            createdToken.addAccount(createAccount);
        });

        return signInResponse;
    }

    public Long signUp(Account account, AccountSignUpRequest request) {

        Account updateAccount = accountMapper.toEntity(request);

        account.signUp(updateAccount);

        if (hasImageFile(request.getImageFile())) {
            eventPublisher.publishEvent(
                    SavedImageEvent.of(request.getImageFile(), account)
            );
        }

        return account.getId();
    }

    public Long updateAccount(Account account, AccountUpdateRequest request) {
        AccountImage accountImage = account.getAccountImage();
        MultipartFile imageFile = request.getImageFile();

        if (hasImageFile(imageFile)) {
            if (accountImage != null) {
                accountImageService.delete(account);
            }

            eventPublisher.publishEvent(
                    SavedImageEvent.of(request.getImageFile(), account)
            );
        }

        account.update(accountMapper.toEntity(request));
        accountRepository.save(account);

        return account.getId();
    }

    private boolean hasImageFile(MultipartFile imageFile){
        return imageFile != null && !imageFile.isEmpty();
    }

    public Long delete(Account account) {

        petService.deleteAllPetInfo(account);

        commentService.deleteAllComment(account);

        accountClubRepository.deleteAccountClubsByAccountId(account.getId());

        accountRepository.delete(account);

        return account.getId();
    }
}
