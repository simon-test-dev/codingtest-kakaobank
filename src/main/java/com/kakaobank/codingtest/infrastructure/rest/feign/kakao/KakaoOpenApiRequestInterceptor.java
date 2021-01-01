package com.kakaobank.codingtest.infrastructure.rest.feign.kakao;

import feign.RequestInterceptor;
import feign.RequestTemplate;

public class KakaoOpenApiRequestInterceptor implements RequestInterceptor {
    private final String authorization;

    public KakaoOpenApiRequestInterceptor(final String key) {
        this.authorization = "KakaoAK " + key;
    }


    @Override
    public void apply(final RequestTemplate template) {
        template.header("Authorization", authorization);
    }
}
