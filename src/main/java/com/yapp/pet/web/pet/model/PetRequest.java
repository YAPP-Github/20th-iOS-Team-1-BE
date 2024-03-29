package com.yapp.pet.web.pet.model;

import com.yapp.pet.domain.common.PetSizeType;
import com.yapp.pet.domain.pet.entity.PetSex;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PetRequest {

    @NotBlank
    private String name;

    @NotNull
    private Integer year;

    @NotNull
    private Integer month;

    @NotBlank
    private String breed;

    @NotNull
    private PetSex sex;

    private boolean neutering;

    @NotNull
    private PetSizeType sizeType;

    private List<String> tags = new ArrayList<>();

    private MultipartFile imageFile;

    @Builder
    public PetRequest(String name, Integer year, Integer month, String breed, PetSex sex, boolean neutering,
                      PetSizeType sizeType, List<String> tags, MultipartFile imageFile) {
        this.name = name;
        this.year = year;
        this.month = month;
        this.breed = breed;
        this.sex = sex;
        this.neutering = neutering;
        this.sizeType = sizeType;
        this.tags = tags;
        this.imageFile = imageFile;
    }
}