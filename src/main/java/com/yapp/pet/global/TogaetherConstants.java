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
            "swagger", "/api-docs",
            "/auth/apple/callback", "/auth/kakao/callback");


}
