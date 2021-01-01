package com.kakaobank.codingtest.infrastructure.rest.feign.kakao;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class KakaoOpenApiConfiguration {
    @Value("${openapi.kakao.key}")
    private String key;

    @Bean
    public RequestInterceptor requestInterceptor() {
        return new KakaoOpenApiRequestInterceptor(key);
    }
}
