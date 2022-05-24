package com.yapp.pet.global.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.yapp.pet.global.TogaetherConstants.AUTHORIZATION_HEADER;
import static com.yapp.pet.global.TogaetherConstants.AUTHORIZATION_HEADER_BEARER;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

	private final JwtService jwtService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
									FilterChain filterChain) throws ServletException, IOException {

		String jwt = resolveToken(request);
		String requestURI = request.getRequestURI();

		if (StringUtils.hasText(jwt) && jwtService.validateToken(jwt)) {
			JwtAuthentication authentication = jwtService.getAuthentication(jwt);
			SecurityContextHolder.getContext().setAuthentication(authentication);

			log.debug("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);
		} else {
			log.debug("유효한 JWT 토큰이 없습니다, uri: {}", requestURI);
		}

		filterChain.doFilter(request, response);
	}

	private String resolveToken(HttpServletRequest request) {
		final String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(AUTHORIZATION_HEADER_BEARER)) {
			return bearerToken.substring(AUTHORIZATION_HEADER_BEARER.length());
		}

		return null;
	}

}
