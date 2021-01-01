package com.kakaobank.codingtest.infrastructure.rest;

import com.kakaobank.codingtest.domain.model.place.PlaceSearchHandler;
import com.kakaobank.codingtest.infrastructure.rest.feign.naver.NaverOpenApiClient;
import com.kakaobank.codingtest.infrastructure.rest.feign.naver.NaverPlaceResultDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@CacheConfig(cacheNames = "naver")
@Service("placeSearchHandlerNaverImpl")
public class PlaceSearchHandlerNaverImpl implements PlaceSearchHandler {
    private final NaverOpenApiClient client;

    @Cacheable(key = "'search:' + #p0", sync = true)
    @Override
    public Optional<List<String>> search(final String keyword) {
        return client.search(keyword)
                     .map(place -> place.getItems()
                                        .stream()
                                        .map(NaverPlaceResultDTO.ItemDTO::getTitle)
                                        .map(name -> name.replaceAll("<[^>]*>", ""))
                                        .collect(toList()));
    }
}
