package com.yapp.pet.domain.account_image;

import com.yapp.pet.domain.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccountImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_image_id")
    private Long id;

    @Column(nullable = false)
    private String originName;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String s3Key;

    @Column(nullable = false)
    private String path;

    @Builder
    public AccountImage(String originName, String name, String s3Key, String path) {
        this.originName = originName;
        this.name = name;
        this.s3Key = s3Key;
        this.path = path;
    }

    public void update(String originName, String name, String s3Key, String path){
        this.originName = originName;
        this.name = name;
        this.s3Key = s3Key;
        this.path = path;
    }

}
