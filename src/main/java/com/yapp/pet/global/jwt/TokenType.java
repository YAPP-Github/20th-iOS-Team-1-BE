package com.yapp.pet.global.jwt;

public enum TokenType {
    ACCESS, REFRESH;

    public static boolean isRefreshToken(String tokenType) {
        return TokenType.REFRESH.name().equals(tokenType);
    }

}

