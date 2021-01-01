package com.kakaobank.codingtest.infrastructure.rest.feign.naver;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class NaverOpenApiConfiguration {
    @Value("${openapi.naver.client.id}")
    private String id;
    @Value("${openapi.naver.client.secret}")
    private String secret;

    @Bean
    public RequestInterceptor requestInterceptor() {
        return new NaverOpenApiRequestInterceptor(id, secret);
    }
}
