package com.yapp.pet.web.account.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AccountValidationResponse {

    private boolean unique;

    private boolean satisfyLengthCondition;

}