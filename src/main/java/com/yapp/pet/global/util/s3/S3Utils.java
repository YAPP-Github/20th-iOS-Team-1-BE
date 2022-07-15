package com.yapp.pet.global.util.s3;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.yapp.pet.domain.account_image.AccountImage;
import com.yapp.pet.domain.pet_image.PetImage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class S3Utils {

    private final S3Properties s3Properties;

    private AmazonS3 amazonS3Client;

    @PostConstruct
    public void setAmazonS3Client() {
        AWSCredentials credentials
                = new BasicAWSCredentials(s3Properties.getAccessKey(), s3Properties.getSecretKey());

        amazonS3Client = AmazonS3ClientBuilder.standard()
                                              .withCredentials(new AWSStaticCredentialsProvider(credentials))
                                              .withRegion(s3Properties.getRegion())
                                              .build();
    }

    public void deleteToS3(AccountImage accountImage){
        amazonS3Client.deleteObject(s3Properties.getBucket(), accountImage.getS3Key());
    }

    public void deleteToS3(PetImage petImage) {
        amazonS3Client.deleteObject(s3Properties.getBucket(), petImage.getS3Key());
    }

    public List<String> multiUploadToS3(List<MultipartFile> uploadFiles, String filename, String dirname) {
        return uploadFiles.stream()
                .map(file -> putS3(file, filename, dirname))
                .collect(Collectors.toList());
    }

    public String putS3(MultipartFile uploadFile, String filename, String dirname) {
        String s3Key = createS3Key(filename, dirname);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(MediaType.IMAGE_PNG_VALUE);
        metadata.setContentLength(uploadFile.getSize());

        try {
            amazonS3Client.putObject(
                    new PutObjectRequest(s3Properties.getBucket(), s3Key, uploadFile.getInputStream(), metadata)
                            .withCannedAcl(CannedAccessControlList.PublicRead));

        } catch (IOException e){
            e.printStackTrace();
        }

        return amazonS3Client
                .getUrl(s3Properties.getBucket(), s3Key)
                .toString();
    }

    public String createS3Key(String filename, String dirname){
        StringBuilder sb = new StringBuilder();

        return sb.append(dirname)
                 .append("/")
                 .append(filename)
                 .toString();
    }

    public String createFilename(String origFilename){
        StringBuilder sb = new StringBuilder();

        return sb.append(System.currentTimeMillis())
                 .append("_")
                 .append(origFilename)
                 .toString();
    }

    public String getImagePath(String path) {
        return amazonS3Client.getUrl(s3Properties.getBucket(), path).toString();
    }

}