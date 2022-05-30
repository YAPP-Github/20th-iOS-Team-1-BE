package com.yapp.pet.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class OpenApiConfig{

    @Bean
    public OpenAPI openAPI(@Value("${springdoc.version}") String appVersion) {
        Info info = new Info().title("YAPP20-BE-ToGaether API").version(appVersion)
                              .description("모임 기반 플랫폼 ToGaether API 문서")
                              .license(new License().name("Apache License Version 2.0").url("[http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0)"));

        return new OpenAPI()
                .components(new Components())
                .info(info);
    }
}