package com.yapp.pet.web.pet;

import com.yapp.pet.domain.account.entity.Account;
import com.yapp.pet.domain.pet.service.PetService;
import com.yapp.pet.global.annotation.AuthAccount;
import com.yapp.pet.web.pet.model.PetRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class PetController {

    private final PetService petService;

    @PostMapping("/pets")
    public long register(@AuthAccount Account account, @Valid @ModelAttribute PetRequest request) {
        log.info("tags = {}", request.getTags());
        return petService.addPet(account, request);
    }
}
