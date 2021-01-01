package com.kakaobank.codingtest.infrastructure.rest.feign.naver;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.kakaobank.codingtest.infrastructure.rest.feign.kakao.KakaoOpenApiClient;
import com.kakaobank.codingtest.infrastructure.rest.feign.kakao.KakaoOpenApiRequestInterceptor;
import com.kakaobank.codingtest.infrastructure.rest.feign.kakao.KakaoPlaceResultDTO;
import com.kakaobank.codingtest.infrastructure.rest.feign.kakao.KakaoPlaceResultDTO.DocumentDTO;
import feign.Contract;
import feign.Feign;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.httpclient.ApacheHttpClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.io.IOException;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.apache.commons.io.IOUtils.toByteArray;
import static org.assertj.core.api.Assertions.assertThat;

@SpringJUnitConfig(classes = {KakaoOpenApiClientTest.Config.class,
                              FeignClientsConfiguration.class})
class KakaoOpenApiClientTest {
    @Autowired
    private Encoder encoder;
    @Autowired
    private Decoder decoder;
    @Autowired
    private Contract contract;
    @Value("classpath:openapi/kakao.json")
    private Resource resource;
    private WireMockServer server;
    private KakaoOpenApiClient client;

    @BeforeEach
    void setUp() {
        server = new WireMockServer(wireMockConfig().bindAddress("localhost").dynamicPort());
        server.start();
        client = Feign.builder()
                      .client(new ApacheHttpClient())
                      .encoder(encoder)
                      .decoder(decoder)
                      .contract(contract)
                      .requestInterceptor(new KakaoOpenApiRequestInterceptor("key"))
                      .target(KakaoOpenApiClient.class, "http://localhost:" + server.port());
    }

    @AfterEach
    void tearDown() {
        server.stop();
    }

    @Test
    void search() throws IOException {
        server.stubFor(get(urlPathEqualTo("/v2/local/search/keyword.json"))
                           .withQueryParam("size", equalTo("10"))
                           .withQueryParam("query", equalTo("곱창"))
                           .withHeader("Authorization", equalTo("KakaoAK key"))
                           .willReturn(aResponse().withStatus(200)
                                                  .withHeader("Content-Type", "application/json")
                                                  .withBody(toByteArray(resource.getInputStream()))));
        final var result = new KakaoPlaceResultDTO(List.of(new DocumentDTO("제일곱창"),
                                                           new DocumentDTO("세광양대창 교대본점 본관"),
                                                           new DocumentDTO("은하곱창"),
                                                           new DocumentDTO("해성막창집 본점"),
                                                           new DocumentDTO("대낚식당"),
                                                           new DocumentDTO("청어람 본점"),
                                                           new DocumentDTO("약수터식당"),
                                                           new DocumentDTO("별미곱창 본점"),
                                                           new DocumentDTO("강남곱"),
                                                           new DocumentDTO("양미옥 을지로본점"),
                                                           new DocumentDTO("곱 마포점"),
                                                           new DocumentDTO("기찻길풍경야채곱창"),
                                                           new DocumentDTO("교대곱창"),
                                                           new DocumentDTO("별양집"),
                                                           new DocumentDTO("우정양곱창")));
        assertThat(client.search("곱창")).contains(result);
    }

    @TestConfiguration
    static class Config {
        @Bean
        HttpMessageConverters httpMessageConverters() {
            return new HttpMessageConverters(new MappingJackson2HttpMessageConverter());
        }
    }
}
