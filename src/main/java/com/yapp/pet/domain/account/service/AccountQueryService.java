package com.yapp.pet.domain.account.service;

import com.yapp.pet.domain.account.entity.Account;
import com.yapp.pet.domain.account.repository.AccountRepository;
import com.yapp.pet.domain.account_image.AccountImageService;
import com.yapp.pet.domain.pet.entity.Pet;
import com.yapp.pet.domain.pet.repository.PetRepository;
import com.yapp.pet.domain.token.repository.TokenRepository;
import com.yapp.pet.global.jwt.JwtService;
import com.yapp.pet.global.mapper.AccountMapper;
import com.yapp.pet.web.account.model.AccountValidationResponse;
import com.yapp.pet.web.account.model.MyPageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountQueryService {

    private final JwtService jwtService;
    private final AccountImageService accountImageService;

    private final AccountMapper accountMapper;

    private final AccountRepository accountRepository;
    private final TokenRepository tokenRepository;
    private final PetRepository petRepository;

    public MyPageResponse getMyPageInfo(Account account) {

        Account findAccount = accountRepository.findAccount(account.getToken().getUniqueIdBySocial());

        List<Pet> findPets = petRepository.findPetsByAccountId(account.getId());

        return MyPageResponse.of(findAccount, findPets);
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

}
