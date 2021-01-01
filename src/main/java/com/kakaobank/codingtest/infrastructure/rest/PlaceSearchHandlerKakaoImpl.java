package com.kakaobank.codingtest.infrastructure.rest;

import com.kakaobank.codingtest.domain.model.place.PlaceSearchHandler;
import com.kakaobank.codingtest.infrastructure.rest.feign.kakao.KakaoOpenApiClient;
import com.kakaobank.codingtest.infrastructure.rest.feign.kakao.KakaoPlaceResultDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@CacheConfig(cacheNames = "kakao")
@Service("placeSearchHandlerKakaoImpl")
public class PlaceSearchHandlerKakaoImpl implements PlaceSearchHandler {
    private final KakaoOpenApiClient client;

    @Cacheable(key = "'search:' + #p0", sync = true)
    @Override
    public Optional<List<String>> search(final String keyword) {
        return client.search(keyword)
                     .map(place -> place.getDocuments()
                                        .stream()
                                        .map(KakaoPlaceResultDTO.DocumentDTO::getName)
                                        .collect(toList()));
    }

}
