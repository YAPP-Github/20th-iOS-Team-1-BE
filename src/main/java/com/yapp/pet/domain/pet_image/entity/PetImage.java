package com.yapp.pet.domain.pet_image.entity;

import com.yapp.pet.domain.common.BaseEntity;
import com.yapp.pet.domain.pet.entity.Pet;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

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

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "pet_id")
    private Pet pet;

    @Builder
    public PetImage(String originName, String name, String path, Pet pet) {
        this.originName = originName;
        this.name = name;
        this.path = path;
        this.pet = pet;
    }
}
