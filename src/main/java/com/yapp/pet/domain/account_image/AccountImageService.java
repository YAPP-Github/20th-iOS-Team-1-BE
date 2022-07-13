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
@Transactional
public class AccountImageService {

    private final S3Utils s3Utils;

    private final AccountImageRepository accountImageRepository;

    public void create(MultipartFile imageFile, Account account) {

        String origFilename = imageFile.getOriginalFilename();
        String filename = s3Utils.createFilename(origFilename);

        s3Utils.putS3(imageFile, filename, S3_ACCOUNT_DIR_NAME);

        String imageUrl = s3Utils.getImageUrl(filename, S3_ACCOUNT_DIR_NAME);
        String s3Key = s3Utils.createS3Key(filename, S3_ACCOUNT_DIR_NAME);

        AccountImage accountImage = AccountImage.builder()
                .name(filename)
                .originName(origFilename)
                .s3Key(s3Key)
                .path(imageUrl)
                .build();

        accountImageRepository.save(accountImage);
        account.addImage(accountImage);
    }

    public void delete(Account account){
        accountImageRepository.delete(account.getAccountImage());

        s3Utils.deleteToS3(account.getAccountImage());
    }

}
