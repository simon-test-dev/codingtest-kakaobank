package com.kakaobank.codingtest.infrastructure.rest.feign.naver;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class NaverOpenApiFallbackFactory implements FallbackFactory<NaverOpenApiClient> {
    @Override
    public NaverOpenApiClient create(final Throwable cause) {
        return new NaverOpenApiClient() {
            @Override
            public Optional<NaverPlaceResultDTO> search(final String query) {
                log.error("[FALLBACK] naver api error, query:`{}`", query, cause);
                return Optional.empty();
            }
        };
    }
}
