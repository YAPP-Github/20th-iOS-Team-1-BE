package com.yapp.pet.web.account.model;

import com.yapp.pet.domain.account.entity.AccountSex;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
public class AccountSignUpRequest {

    @NotBlank
    private String nickname;

    @NotNull
    @Min(value = 1) @Max(value = 120)
    private Integer age;

    @NotNull
    private AccountSex sex;

    @NotBlank
    private String city;

    @NotBlank
    private String detail;

}
