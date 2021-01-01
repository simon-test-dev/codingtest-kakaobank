package com.kakaobank.codingtest.infrastructure.rest.feign.naver;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@FeignClient(name = "naver",
             url = "${openapi.naver.host}",
             decode404 = true,
             fallbackFactory = NaverOpenApiFallbackFactory.class,
             configuration = NaverOpenApiConfiguration.class)
public interface NaverOpenApiClient {
    @GetMapping("/v1/search/local.json?display=5")
    Optional<NaverPlaceResultDTO> search(@RequestParam("query") String query);
}
