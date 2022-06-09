package com.yapp.pet.global.util.s3;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
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

    public List<String> uploadToS3(List<MultipartFile> uploadFile, String dirName) {
        return uploadFile.stream()
                .map(file -> putS3(file, dirName))
                .collect(Collectors.toList());
    }

    private String putS3(MultipartFile uploadFile, String dirName) {
        String key = createKey(uploadFile, dirName);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(MediaType.IMAGE_PNG_VALUE);
        metadata.setContentLength(uploadFile.getSize());

        try {
            amazonS3Client.putObject(
                    new PutObjectRequest(s3Properties.getBucket(), key, uploadFile.getInputStream(), metadata)
                            .withCannedAcl(CannedAccessControlList.PublicRead));

        } catch (IOException e){
            e.printStackTrace();
        }

        return amazonS3Client
                .getUrl(s3Properties.getBucket(), key)
                .toString();
    }

    private String createKey(MultipartFile uploadFile, String dirName){
        StringBuilder sb = new StringBuilder();

        return sb.append(dirName)
                .append("/")
                .append(createFileName(uploadFile.getOriginalFilename()))
                .toString();
    }

    public String createFileName(String origFilename){
        StringBuilder sb = new StringBuilder();

        return sb.append(System.currentTimeMillis())
                .append("_")
                .append(origFilename)
                .toString();
    }

}
