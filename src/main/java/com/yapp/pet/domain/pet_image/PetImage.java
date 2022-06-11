package com.yapp.pet.domain.pet_image;

import com.yapp.pet.domain.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PetImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pet_image_id")
    private Long id;

    @Column(nullable = false)
    private String originName;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String path;

    @Builder
    public PetImage(String originName, String name, String path) {
        this.originName = originName;
        this.name = name;
        this.path = path;
    }
}
