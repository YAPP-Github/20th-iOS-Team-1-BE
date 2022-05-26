package com.yapp.pet.web.token;

import com.yapp.pet.global.jwt.JwtAuthentication;
import com.yapp.pet.global.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TokenTestController {

    private final JwtService jwtService;

    @PostMapping("/access-token")
    public String getAccessToken(){
        String[] arr = {"test", "test"};
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(arr)
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        JwtAuthentication authentication = new JwtAuthentication("access", "access", authorities);

        return jwtService.createAccessToken(authentication);
    }

    @PostMapping("/refresh-token")
    public String getRefreshToken(){
        String[] arr = {"test", "test"};
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(arr)
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        JwtAuthentication authentication = new JwtAuthentication("refresh", "refresh", authorities);

        return jwtService.createRefreshToken(authentication);
    }

    @PostMapping("/")
    public String test(){
        return "ok";
    }

}
