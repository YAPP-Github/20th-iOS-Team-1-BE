package com.yapp.pet.domain.account.service;

import com.yapp.pet.domain.account.entity.Account;
import com.yapp.pet.domain.account.repository.AccountRepository;
import com.yapp.pet.domain.pet.entity.Pet;
import com.yapp.pet.domain.pet.repository.PetRepository;
import com.yapp.pet.web.account.model.AccountValidationResponse;
import com.yapp.pet.web.account.model.MyPageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountQueryService {

    private final AccountRepository accountRepository;
    private final PetRepository petRepository;

    public MyPageResponse getMyPageInfo(Account account, String nickname) {

        Account findAccount = accountRepository.findByNickname(nickname).orElseThrow(EntityNotFoundException::new);

        List<Pet> findPets = petRepository.findPetsByAccountId(findAccount.getId());

        MyPageResponse response = MyPageResponse.of(findAccount, findPets);

        if (account.isMe(findAccount)) {
            response.setMyPage(true);
        }

        return response;
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
