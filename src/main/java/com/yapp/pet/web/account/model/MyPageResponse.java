package com.yapp.pet.web.account.model;

import com.yapp.pet.domain.account.entity.Account;
import com.yapp.pet.domain.account.entity.AccountSex;
import com.yapp.pet.domain.common.Category;
import com.yapp.pet.domain.pet.entity.Pet;
import com.yapp.pet.domain.pet.entity.PetSex;
import com.yapp.pet.domain.pet_tag.PetTag;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class MyPageResponse {

    private AccountInfoResponse accountInfo;

    private List<PetInfoResponse> petInfos;

    public MyPageResponse(Account account, List<Pet> pets) {

        this.accountInfo = new AccountInfoResponse(account);

        this.petInfos = pets.stream()
                .map(PetInfoResponse::new)
                .collect(Collectors.toList());
    }

    public static MyPageResponse of(Account account, List<Pet> pets) {
        return new MyPageResponse(account, pets);
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class AccountInfoResponse {

        private String nickname;

        private String address;

        private String age;

        private AccountSex sex;

        private String selfIntroduction;

        private List<Category> interestCategories = new ArrayList<>();

        private String imageUrl;

        public AccountInfoResponse(Account account) {
            this.nickname = account.getNickname();
            this.address = account.getAddress().getCity() + " " + account.getAddress().getDetail();
            this.age = account.getAge() + "살";
            this.sex = account.getSex();
            this.selfIntroduction = account.getSelfIntroduction();

            if (account.getInterestCategories() != null) {
                this.interestCategories.addAll(account.getInterestCategories());
            }

            if (account.getAccountImage() != null) {
                this.imageUrl = account.getAccountImage().getPath();
            }
        }

    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class PetInfoResponse {

        private String nickname;

        private String breed;

        private String age;

        private PetSex sex;

        private List<String> tags = new ArrayList<>();

        private String imageUrl;

        public PetInfoResponse(Pet pet) {
            this.nickname = pet.getName();
            this.breed = pet.getBreed();
            this.age = pet.getAge().getAge();
            this.sex = pet.getSex();

            if (pet.getTags() != null && pet.getTags().size() != 0) {
                this.tags.addAll(pet.getTags().stream()
                        .map(PetTag::getName)
                        .collect(Collectors.toList()));
            }

            if (pet.getPetImage() != null) {
                this.imageUrl = pet.getPetImage().getPath();
            }
        }

    }

}
