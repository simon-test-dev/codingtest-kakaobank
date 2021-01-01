package com.kakaobank.codingtest.infrastructure.rest.feign.naver;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NaverOpenApiRequestInterceptor implements RequestInterceptor {
    private final String id;
    private final String secret;

    @Override
    public void apply(final RequestTemplate template) {
        template.header("X-Naver-Client-Id", id)
                .header("X-Naver-Client-Secret", secret);
    }
}
