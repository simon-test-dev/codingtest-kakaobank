package com.kakaobank.codingtest.infrastructure.rest.feign.kakao;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@FeignClient(name = "kakao",
             url = "${openapi.kakao.host}",
             decode404 = true,
             fallbackFactory = KakaoOpenApiFallbackFactory.class,
             configuration = KakaoOpenApiConfiguration.class)
public interface KakaoOpenApiClient {
    @GetMapping("/v2/local/search/keyword.json?size=10")
    Optional<KakaoPlaceResultDTO> search(@RequestParam("query") String query);
}
