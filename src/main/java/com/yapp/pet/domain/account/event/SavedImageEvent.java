package com.yapp.pet.domain.account.event;

import com.yapp.pet.domain.account.entity.Account;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor
public class SavedImageEvent {

    private MultipartFile imageFile;

    private Account account;

    public SavedImageEvent(MultipartFile imageFile, Account account) {
        this.imageFile = imageFile;
        this.account = account;
    }

    public static SavedImageEvent of(MultipartFile imageFile, Account account){
        return new SavedImageEvent(imageFile, account);
    }
}
