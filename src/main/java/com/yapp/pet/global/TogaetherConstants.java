package com.yapp.pet.global;

import java.util.List;

public final class TogaetherConstants {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String AUTHORIZATION_HEADER_BEARER = "Bearer ";

    public static final String AUTHORITIES_KEY = "auth";
    public static final String JWT_HEADER_PARAM_TYPE = "typ";
    public static final String ROLE = "USER";

    public static final String ERROR_LOG_MESSAGE = "Exception = {} , message = {}";
    public static final String CONTENT_TYPE = "application/json";
    public static final String CHARACTER_ENCODING = "UTF-8";

    public static final List<String> ALLOWED_URLS
            = List.of("/health-check", "/test",
            "swagger", "/api-docs", "/openapi.yml", "swagger-ui",
            "/auth/apple/callback", "/auth/kakao/callback");

    public static final String KAKAO_CONTENT_TYPE = "application/x-www-form-urlencoded;charset=utf-8";
    public static final String GRANT_TYPE = "authorization_code";
    public static final String LOCAL_REDIRECT_URI = "http://localhost:8080/auth/kakao/callback";
    public static final String DEV_REDIRECT_URI = "https://yapp-togather.com:443/auth/kakao/callback";

    public static final String S3_ACCOUNT_DIR_NAME = "account";

    public static final String ELIGIBLE_BREEDS_ALL = "상관없음";

    public static final double FEET_TO_METER = 1000;

    public static final int EARTH = 6371;
}
