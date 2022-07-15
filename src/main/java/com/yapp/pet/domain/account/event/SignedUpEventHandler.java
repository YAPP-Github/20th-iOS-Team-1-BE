package com.yapp.pet.domain.account.event;

import com.yapp.pet.domain.account_image.AccountImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SignedUpEventHandler {

    private final AccountImageService accountImageService;

    @EventListener
    public void saveImage(SignedUpEvent event){
        accountImageService.create(event.getImageFile(), event.getAccount());
    }

}
