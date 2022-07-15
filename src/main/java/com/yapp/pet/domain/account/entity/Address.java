package com.yapp.pet.domain.account.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Address {

    private String city;
    private String detail;

    public void updateCity(String city){
        this.city = city;
    }

    public void updateDetail(String detail){
        this.detail = detail;
    }

}
