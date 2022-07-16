package com.yapp.pet.web.pet;

import com.yapp.pet.domain.account.entity.Account;
import com.yapp.pet.domain.pet.service.PetService;
import com.yapp.pet.global.annotation.AuthAccount;
import com.yapp.pet.web.pet.model.PetRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
    public ResponseEntity<Long> register(@AuthAccount Account account, @Valid @ModelAttribute PetRequest request) {
        log.info("tags = {}", request.getTags());

        Long petId = petService.addPet(account, request);

        return ResponseEntity.ok(petId);
    }

    @DeleteMapping("/pets/{pet-id}")
    public ResponseEntity<Long> delete(@AuthAccount Account account, @PathVariable("pet-id") long petId) {
        Long deletedPetId = petService.deletePetInfo(petId);

        return ResponseEntity.ok(deletedPetId);
    }

    @PutMapping("/pets/{pet-id}")
    public ResponseEntity<Long> update(@AuthAccount Account account, @PathVariable("pet-id") long petId,
                                       @Valid @ModelAttribute PetRequest request) {

        Long updatedPetId = petService.updatePetInfo(petId, request);

        return ResponseEntity.ok(updatedPetId);
    }
}
