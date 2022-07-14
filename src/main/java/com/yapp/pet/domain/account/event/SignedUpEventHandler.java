package com.yapp.pet.domain.account.event;

import com.yapp.pet.domain.account_image.AccountImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class SignedUpEventHandler {

    private final AccountImageService accountImageService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveImage(SignedUpEvent event){
        accountImageService.create(event.getImageFile(), event.getAccount());
    }

}
