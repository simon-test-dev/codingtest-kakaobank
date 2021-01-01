package com.kakaobank.codingtest.infrastructure.rest;

import com.kakaobank.codingtest.infrastructure.rest.feign.naver.NaverOpenApiClient;
import com.kakaobank.codingtest.infrastructure.rest.feign.naver.NaverPlaceResultDTO;
import com.kakaobank.codingtest.infrastructure.rest.feign.naver.NaverPlaceResultDTO.ItemDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

class PlaceSearchHandlerNaverImplTest {
    private NaverOpenApiClient client;
    private PlaceSearchHandlerNaverImpl handler;

    @BeforeEach
    void setUp() {
        client = mock(NaverOpenApiClient.class);
        handler = new PlaceSearchHandlerNaverImpl(client);
    }

    @Test
    void testEmpty() {
        doReturn(Optional.empty()).when(client).search("keyword");
        assertThat(handler.search("keyword")).isEmpty();
    }

    @Test
    void testSearch() {
        final var result = new NaverPlaceResultDTO();
        final var document1 = new ItemDTO();
        document1.setTitle("keyword1");
        final var document2 = new ItemDTO();
        document2.setTitle("keyword2");
        result.setItems(List.of(document1, document2));
        doReturn(Optional.of(result)).when(client).search("keyword");
        assertThat(handler.search("keyword")).contains(List.of("keyword1", "keyword2"));
    }


    @Test
    void testSearchRemoveBoldTag() {
        final var result = new NaverPlaceResultDTO();
        final var document1 = new ItemDTO();
        document1.setTitle("some <b>keyword</b>1");
        final var document2 = new ItemDTO();
        document2.setTitle("<b>keyword</b>2 after");
        result.setItems(List.of(document1, document2));
        doReturn(Optional.of(result)).when(client).search("keyword");
        assertThat(handler.search("keyword")).contains(List.of("some keyword1", "keyword2 after"));
    }
}
