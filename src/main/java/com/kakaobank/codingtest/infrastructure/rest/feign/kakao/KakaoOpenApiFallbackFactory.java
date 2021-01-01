package com.kakaobank.codingtest.infrastructure.rest.feign.kakao;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class KakaoOpenApiFallbackFactory implements FallbackFactory<KakaoOpenApiClient> {
    @Override
    public KakaoOpenApiClient create(final Throwable cause) {
        return new KakaoOpenApiClient() {
            @Override
            public Optional<KakaoPlaceResultDTO> search(final String query) {
                log.error("[FALLBACK] kakao api error, query:`{}`", query, cause);
                return Optional.empty();
            }
        };
    }
}
