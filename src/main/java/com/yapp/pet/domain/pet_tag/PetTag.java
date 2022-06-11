package com.yapp.pet.domain.pet_tag;

import com.yapp.pet.domain.common.BaseEntity;
import com.yapp.pet.domain.pet.entity.Pet;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PetTag extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pet_tag_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "pet_id")
    private Pet pet;

    @Column(nullable = false, length = 10)
    private String name;

    private PetTag(Pet pet, String name) {
        this.pet = pet;
        this.name = name;
    }

    public static PetTag of(Pet pet, String name) {
        return new PetTag(pet, name);
    }

}
