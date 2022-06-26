package com.yapp.pet.web.oauth.apple;

import com.yapp.pet.web.oauth.apple.model.ApplePublicKeyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(url = "https://appleid.apple.com", name = "AppleClient")
public interface AppleClient {

    @GetMapping(value = "/auth/keys", consumes = "application/json")
    ApplePublicKeyResponse getApplePublicKey();
}
