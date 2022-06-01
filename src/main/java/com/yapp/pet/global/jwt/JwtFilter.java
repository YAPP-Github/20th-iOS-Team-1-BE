package com.yapp.pet.global.jwt;

import com.yapp.pet.global.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

	private final JwtService jwtService;

	@Override
	protected void doFilterInternal(HttpServletRequest httpRequest, HttpServletResponse response,
									FilterChain filterChain) throws ServletException, IOException {

		String jwt = JwtUtils.resolveToken(httpRequest);

		if (!StringUtils.hasText(jwt)) {
			throw new RuntimeException();
		}

		jwtService.validateToken(jwt);

		filterChain.doFilter(httpRequest, response);
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException{
		String path = request.getRequestURI();
		return path.equals("/auth/apple/callback");
	}

}
