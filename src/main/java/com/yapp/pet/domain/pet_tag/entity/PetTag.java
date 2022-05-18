package com.yapp.pet.domain.pet_tag.entity;

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
@Table(name = "pet_prefer_tag")
public class PetTag extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pet_tag_id")
    private Long id;

    @Column(nullable = false, length = 10)
    private String name;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "pet_id")
    private Pet pet;

    private PetTag(String name, Pet pet) {
        this.name = name;
        this.pet = pet;
    }

    public static PetTag of(String name, Pet pet) {
        return new PetTag(name, pet);
    }
}
