package com.yapp.pet.domain.account.service;

import com.yapp.pet.domain.account.AccountRepository;
import com.yapp.pet.domain.account.entity.Account;
import com.yapp.pet.global.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final JwtService jwtService;

    private final AccountRepository accountRepository;

    public String getJwtToken(Long accountId){
        Account account = accountRepository.findById(accountId).orElseThrow(EntityNotFoundException::new);

        return jwtService.createAccessToken(account);
    }

}
