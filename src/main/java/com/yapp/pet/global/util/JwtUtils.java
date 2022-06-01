package com.yapp.pet.global.util;

import org.springframework.util.StringUtils;
import org.springframework.web.context.request.NativeWebRequest;

import javax.servlet.http.HttpServletRequest;

import static com.yapp.pet.global.TogaetherConstants.AUTHORIZATION_HEADER;
import static com.yapp.pet.global.TogaetherConstants.AUTHORIZATION_HEADER_BEARER;

public class JwtUtils {

    public static String resolveToken(HttpServletRequest request) {
        final String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(AUTHORIZATION_HEADER_BEARER)) {
            return bearerToken.substring(AUTHORIZATION_HEADER_BEARER.length());
        }

        return null;
    }

    public static String resolveTokenByWebRequest(NativeWebRequest request) {
        final String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(AUTHORIZATION_HEADER_BEARER)) {
            return bearerToken.substring(AUTHORIZATION_HEADER_BEARER.length());
        }

        return null;
    }

}
