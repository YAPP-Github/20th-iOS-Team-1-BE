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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.yapp.pet.global.TogaetherConstants.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExceptionHandlerFilter extends OncePerRequestFilter {

	private final ObjectMapper objectMapper;

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
			sendErrorMessage(response, e);
		}

	}

	private void sendErrorMessage(HttpServletResponse response, BusinessException e) throws IOException {
		response.setCharacterEncoding(CHARACTER_ENCODING);
		response.setContentType(CONTENT_TYPE);
		response.setStatus(e.getHttpStatus().value());
		response.getWriter().write(objectMapper.writeValueAsString(ExceptionResponseInfo.from(e)));

		log.error(ERROR_LOG_MESSAGE, e.getClass().getSimpleName(), e.getLocalizedMessage());
	}

	private void sendErrorMessage(HttpServletResponse response, RuntimeException e) throws IOException {
		response.setCharacterEncoding(CHARACTER_ENCODING);
		response.setContentType(CONTENT_TYPE);
		response.setStatus(HttpStatus.BAD_REQUEST.value());
		response.getWriter().write("토큰이 존재하지 않습니다.");

		log.error(ERROR_LOG_MESSAGE, e.getClass().getSimpleName(), e.getLocalizedMessage());
	}

}
