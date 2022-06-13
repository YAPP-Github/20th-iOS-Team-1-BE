package com.yapp.pet.global.mapper;

import com.yapp.pet.domain.account.entity.Account;
import com.yapp.pet.web.account.model.AccountSignUpRequest;
import com.yapp.pet.web.account.model.AccountUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ValueMapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(target = "address.city", source = "city")
    @Mapping(target = "address.detail", source = "detail")
    @ValueMapping(target = "sex", source = "sex")
    Account toEntity(AccountSignUpRequest signUpRequest);

    @Mapping(target = "address.city", source = "city")
    @Mapping(target = "address.detail", source = "detail")
    Account toEntity(AccountUpdateRequest accountUpdateRequest);

}
