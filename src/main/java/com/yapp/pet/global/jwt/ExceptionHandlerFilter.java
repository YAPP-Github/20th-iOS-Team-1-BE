package com.yapp.pet.global.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yapp.pet.global.exception.common.BusinessException;
import com.yapp.pet.global.exception.common.response.ExceptionResponseInfo;
import com.yapp.pet.global.exception.jwt.ExpiredJwtTokenException;
import com.yapp.pet.global.exception.jwt.InvalidJwtSignatureException;
import com.yapp.pet.global.exception.jwt.InvalidJwtTokenException;
import com.yapp.pet.global.exception.jwt.UnsupportedJwtTokenException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExceptionHandlerFilter extends OncePerRequestFilter {

	private final ObjectMapper objectMapper;
	private static final String ERROR_LOG_MESSAGE = "Exception = {} , message = {}";
	private static final String CONTENT_TYPE = "application/json";

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
									FilterChain filterChain) throws ServletException, IOException {

		try {
			filterChain.doFilter(request, response);
		} catch (InvalidJwtSignatureException e) {
			sendErrorMessage(response, e);
		} catch (ExpiredJwtTokenException e) {
			sendErrorMessage(response, e);
		} catch (UnsupportedJwtTokenException e) {
			sendErrorMessage(response, e);
		} catch (InvalidJwtTokenException e) {
			sendErrorMessage(response, e);
		} catch (RuntimeException e) {
			log.error(e.getMessage());
		}

	}

	private void sendErrorMessage(HttpServletResponse response, BusinessException e) throws IOException {
		response.setContentType(CONTENT_TYPE);
		response.setStatus(e.getHttpStatus().value());
		response.getWriter().write(objectMapper.writeValueAsString(ExceptionResponseInfo.from(e)));

		log.error(ERROR_LOG_MESSAGE, e.getClass().getSimpleName(),
				e.getLocalizedMessage());
	}

}
