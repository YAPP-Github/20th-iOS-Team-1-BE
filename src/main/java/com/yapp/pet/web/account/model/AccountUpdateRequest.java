package com.yapp.pet.web.account.model;

import com.yapp.pet.domain.account.entity.AccountSex;
import com.yapp.pet.domain.common.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class AccountUpdateRequest {

    private String nickname;

    private MultipartFile imageFile;

    @Min(value = 1) @Max(value = 120)
    private Integer age;

    private AccountSex sex;

    private String city;

    private String detail;

    private String selfIntroduction;

    private List<Category> interestCategories = new ArrayList<>();

}
