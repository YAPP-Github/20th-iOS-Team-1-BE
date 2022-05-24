package com.yapp.pet.global.jwt;

public enum TokenType {

    ACCESS, REFRESH;

    public static boolean isAccessToken(String tokenType) {
        return TokenType.ACCESS.name().equals(tokenType);
    }

}
