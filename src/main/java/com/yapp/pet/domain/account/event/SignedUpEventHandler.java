package com.yapp.pet.domain.account.event;

import com.yapp.pet.domain.account_image.AccountImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SignedUpEventHandler {

    private final AccountImageService accountImageService;

    @EventListener
    @Async
    public void saveImage(SignedUpEvent event){
        accountImageService.create(event.getImageFile(), event.getAccount());
    }

}
