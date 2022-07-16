package com.yapp.pet.domain.account.event;

import com.yapp.pet.domain.account.service.AccountService;
import com.yapp.pet.domain.account_image.AccountImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class AccountEventHandler {

    private final AccountImageService accountImageService;
    private final AccountService accountService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Async
    public void saveImage(SignedUpEvent event){
        accountImageService.create(event.getImageFile(), event.getAccount());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Async
    public void deleteAccount(AccountDeletedEvent event){
        accountService.delete(event.getAccount());
    }

}
