package com.yapp.pet.domain.account.event;

import com.yapp.pet.domain.account.entity.Account;
import com.yapp.pet.domain.common.EventType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor
public class SignedUpEvent {

    private EventType type;

    private MultipartFile imageFile;

    private Account account;

    public SignedUpEvent(EventType type, MultipartFile imageFile, Account account) {
        this.type = type;
        this.imageFile = imageFile;
        this.account = account;
    }

    public static SignedUpEvent of(EventType type, MultipartFile imageFile, Account account){
        return new SignedUpEvent(type, imageFile, account);
    }
}
