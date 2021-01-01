package com.kakaobank.codingtest.infrastructure.rest;

import com.kakaobank.codingtest.infrastructure.rest.feign.kakao.KakaoOpenApiClient;
import com.kakaobank.codingtest.infrastructure.rest.feign.kakao.KakaoPlaceResultDTO;
import com.kakaobank.codingtest.infrastructure.rest.feign.kakao.KakaoPlaceResultDTO.DocumentDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

class PlaceSearchHandlerKakaoImplTest {
    private KakaoOpenApiClient client;
    private PlaceSearchHandlerKakaoImpl handler;

    @BeforeEach
    void setUp() {
        client = mock(KakaoOpenApiClient.class);
        handler = new PlaceSearchHandlerKakaoImpl(client);
    }

    @Test
    void testEmpty() {
        doReturn(Optional.empty()).when(client).search("keyword");
        assertThat(handler.search("keyword")).isEmpty();
    }

    @Test
    void testSearch() {
        final var result = new KakaoPlaceResultDTO();
        final var document1 = new DocumentDTO();
        document1.setName("keyword1");
        final var document2 = new DocumentDTO();
        document2.setName("keyword2");
        result.setDocuments(List.of(document1, document2));
        doReturn(Optional.of(result)).when(client).search("keyword");
        assertThat(handler.search("keyword")).contains(List.of("keyword1", "keyword2"));
    }
}
