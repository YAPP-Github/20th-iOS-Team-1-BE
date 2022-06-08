package com.yapp.pet.domain.account_image;

import com.yapp.pet.domain.account.entity.Account;
import com.yapp.pet.global.util.s3.S3Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.yapp.pet.global.TogaetherConstants.S3_ACCOUNT_DIR_NAME;

@Service
@RequiredArgsConstructor
public class AccountImageService {

    private final S3Utils s3Utils;

    private final AccountImageRepository accountImageRepository;

    @Transactional
    public void createAccountImages(Account account, List<MultipartFile> imageFiles) {
        List<String> s3Urls = s3Utils.uploadToS3(imageFiles, S3_ACCOUNT_DIR_NAME);

        for (int index = 0; index < imageFiles.size(); index++) {
            String origFilename = imageFiles.get(index).getOriginalFilename();
            String filename = s3Utils.createFileName(origFilename);
            String filePath = s3Urls.get(index);

            AccountImage accountImage = AccountImage.builder()
                    .account(account)
                    .name(filename)
                    .originName(origFilename)
                    .path(filePath)
                    .build();

            accountImageRepository.save(accountImage);
        }
    }

}
