package com.yapp.pet.global;

import java.util.List;

public final class TogaetherConstants {

    //JWT
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String AUTHORIZATION_HEADER_BEARER = "Bearer ";
    public static final String AUTHORITIES_KEY = "auth";
    public static final String JWT_HEADER_PARAM_TYPE = "typ";
    public static final String ROLE = "USER";

    //Exception
    public static final String ERROR_LOG_MESSAGE = "Exception = {} , message = {}";
    public static final String CONTENT_TYPE = "application/json";
    public static final String CHARACTER_ENCODING = "UTF-8";

    //white list
    public static final List<String> ALLOWED_URLS
            = List.of("/health-check", "/test",
            "swagger", "/api-docs", "/openapi.yml", "swagger-ui",
            "/auth/apple", "/auth/kakao/callback");

    //OAuth
    public static final String LOCAL_REDIRECT_URI = "http://localhost:8080/auth/kakao/callback";
    public static final String DEV_REDIRECT_URI = "https://yapp-togather.com:443/auth/kakao/callback";
    public static final String KAKAO_CONTENT_TYPE = "application/x-www-form-urlencoded;charset=utf-8";
    public static final String GRANT_TYPE = "authorization_code";

    //S3
    public static final String S3_ACCOUNT_DIR_NAME = "account";
    public static final String S3_PET_DIR_NAME = "pet";

    //distance
    public static final double FEET_TO_METER = 1000;

    //pet age
    public static final int EARTH = 6371;
    public static final int YEAR_TO_MONTH = 12;

    //report
    public static final int NUMBER_OF_LIMIT_REPORTS = 3;

    //redis distributed lock
    public static final String PARTICIPATE_CLUB_LOCK_NAME = "CLUB:participation";
    public static final int WAIT_TIME = 2;
    public static final int LEASE_TIME = 5;
}
