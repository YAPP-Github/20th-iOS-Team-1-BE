package com.yapp.pet.domain.pet.entity;

import com.yapp.pet.domain.account.entity.Account;
import com.yapp.pet.domain.common.BaseEntity;
import com.yapp.pet.domain.common.PetSizeType;
import com.yapp.pet.domain.pet_image.PetImage;
import com.yapp.pet.domain.pet_tag.PetTag;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Pet extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pet_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @OneToOne(fetch = LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "pet_image_id")
    private PetImage petImage;

    @OneToMany(mappedBy = "pet", fetch = LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PetTag> tags = new ArrayList<>();

    private String breed;

    @Column(length = 30, nullable = false)
    private String name;

    @Embedded
    private Age age;

    @Enumerated(STRING)
    @Column(nullable = false)
    private PetSex sex;

    private boolean neutering;

    @Enumerated(STRING)
    @Column(nullable = false)
    private PetSizeType sizeType;

    @Builder
    public Pet(Account account, PetImage petImage, String breed, String name, Age age,
               PetSex sex, boolean neutering, PetSizeType sizeType) {
        this.account = account;
        this.petImage = petImage;
        this.breed = breed;
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.neutering = neutering;
        this.sizeType = sizeType;
    }

    public void update(Pet updatePet) {
        name = updatePet.getName();
        age = updatePet.getAge();
        breed = updatePet.getBreed();
        sex = updatePet.getSex();
        neutering = updatePet.isNeutering();
        sizeType = updatePet.getSizeType();
    }

    public void addTag(PetTag petTag) {
        tags.add(petTag);
    }

    public void addImage(PetImage petImage) {
        this.petImage = petImage;
    }

    public void updateAge(Age age) {
        this.age = age;
    }

}