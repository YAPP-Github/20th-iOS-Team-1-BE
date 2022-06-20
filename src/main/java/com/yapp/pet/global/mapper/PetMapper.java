package com.yapp.pet.global.mapper;

import com.yapp.pet.domain.pet.entity.Pet;
import com.yapp.pet.web.pet.model.PetRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ValueMapping;

@Mapper(componentModel = "spring")
public interface PetMapper {

    @Mapping(target = "age.birthYear", source = "year")
    @Mapping(target = "age.birthMonth", source = "month")
    @ValueMapping(target = "sex", source = "sex")
    @ValueMapping(target = "sizeType", source = "sizeType")
    Pet toEntity(PetRequest petRequest);
}
