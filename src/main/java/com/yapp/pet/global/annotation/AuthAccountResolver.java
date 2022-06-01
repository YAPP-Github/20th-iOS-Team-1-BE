package com.yapp.pet.global.annotation;

import com.yapp.pet.domain.account.entity.Account;
import com.yapp.pet.domain.token.entity.Token;
import com.yapp.pet.domain.token.repository.TokenRepository;
import com.yapp.pet.global.exception.Account.AccountNotFoundException;
import com.yapp.pet.global.jwt.JwtService;
import com.yapp.pet.global.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthAccountResolver implements HandlerMethodArgumentResolver {

    private final JwtService jwtService;
    private final TokenRepository tokenRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasAnnotation = parameter.hasParameterAnnotation(AuthAccount.class);
        boolean isAccountType = Account.class.isAssignableFrom(parameter.getParameterType());

        return hasAnnotation && isAccountType;
    }

    @Override // JwtFilter에서 모두 검증하므로, 검증 로직은 추가하지 않음
    public Account resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                   NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        String jwt = JwtUtils.resolveTokenByWebRequest(webRequest);
        String sub = jwtService.getSubject(jwt);
        Token token = tokenRepository.findByUniqueIdBySocial(sub).orElseThrow(AccountNotFoundException::new);

        return token.getAccount();
    }

}
