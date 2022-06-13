package com.yapp.pet.domain.account_image;

import com.yapp.pet.domain.account.entity.Account;
import com.yapp.pet.global.util.s3.S3Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import static com.yapp.pet.global.TogaetherConstants.S3_ACCOUNT_DIR_NAME;

@Service
@RequiredArgsConstructor
public class AccountImageService {

    private final S3Utils s3Utils;

    private final AccountImageRepository accountImageRepository;

    @Transactional
    public AccountImage create(MultipartFile imageFile) {

        String origFilename = imageFile.getOriginalFilename();
        String imageUrl = s3Utils.uploadToS3(imageFile, S3_ACCOUNT_DIR_NAME);
        String filename = s3Utils.createFileName(origFilename);
        String s3Key = s3Utils.createS3Key(imageFile, S3_ACCOUNT_DIR_NAME);

        AccountImage accountImage = AccountImage.builder()
                .name(filename)
                .originName(origFilename)
                .s3Key(s3Key)
                .path(imageUrl)
                .build();

        accountImageRepository.save(accountImage);

        return accountImage;
    }

    @Transactional
    public void delete(Account account){
        accountImageRepository.delete(account.getAccountImage());

        s3Utils.deleteToS3(account.getAccountImage());
    }

}
