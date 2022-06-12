package com.yapp.pet.domain.account;

import com.yapp.pet.domain.account.entity.Account;
import com.yapp.pet.domain.account.repository.AccountRepository;
import com.yapp.pet.domain.account_image.AccountImageService;
import com.yapp.pet.domain.pet.entity.Pet;
import com.yapp.pet.domain.pet.repository.PetRepository;
import com.yapp.pet.domain.token.entity.Social;
import com.yapp.pet.domain.token.entity.Token;
import com.yapp.pet.domain.token.repository.TokenRepository;
import com.yapp.pet.global.jwt.JwtService;
import com.yapp.pet.global.mapper.AccountMapper;
import com.yapp.pet.web.account.model.AccountSignUpRequest;
import com.yapp.pet.web.account.model.AccountValidationResponse;
import com.yapp.pet.web.account.model.MyPageResponse;
import com.yapp.pet.web.oauth.apple.model.SignInResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountService {

    private final JwtService jwtService;
    private final AccountImageService accountImageService;

    private final AccountMapper accountMapper;

    private final AccountRepository accountRepository;
    private final TokenRepository tokenRepository;
    private final PetRepository petRepository;

    @Transactional
    public SignInResponse signIn(String idToken, Social social) {
        SignInResponse signInResponse = new SignInResponse();

        String uniqueIdBySocial = jwtService.getSubject(idToken);

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

    public AccountValidationResponse validateNickname(String nickname){

        AccountValidationResponse response = new AccountValidationResponse();

        response.setUnique(isUnique(nickname));
        response.setSatisfyLengthCondition(isSatisfyLengthCondition(nickname));

        return response;
    }

    private boolean isSatisfyLengthCondition(String nickname){
        return 2 <= nickname.length() && nickname.length() <= 10;
    }

    private boolean isUnique(String nickname){
        return accountRepository.findByNickname(nickname).isEmpty();
    }

    @Transactional
    public Long signUp(Account account, AccountSignUpRequest signUpRequest) {

        Account updateAccount = accountMapper.toEntity(signUpRequest);

        account.signUp(updateAccount);

        if(signUpRequest.getImageFile() != null){
            String url = accountImageService.createAccountImage(signUpRequest.getImageFile());
            account.addImage(url);
        }

        return account.getId();
    }

    public MyPageResponse getMyPageInfo(Account account) {

        Account findAccount = accountRepository.findAccount(account.getToken().getUniqueIdBySocial());

        List<Pet> findPets = petRepository.findPetsByAccountId(account.getId());

        return MyPageResponse.of(findAccount, findPets);
    }

}
