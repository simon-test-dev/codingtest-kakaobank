package com.kakaobank.codingtest.infrastructure.rest.feign.naver;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.kakaobank.codingtest.infrastructure.rest.feign.naver.NaverPlaceResultDTO.ItemDTO;
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

@SpringJUnitConfig(classes = {NaverOpenApiClientTest.Config.class,
                              FeignClientsConfiguration.class})
class NaverOpenApiClientTest {
    @Autowired
    private Encoder encoder;
    @Autowired
    private Decoder decoder;
    @Autowired
    private Contract contract;
    @Value("classpath:openapi/naver.json")
    private Resource resource;
    private WireMockServer server;
    private NaverOpenApiClient client;

    @BeforeEach
    void setUp() {
        server = new WireMockServer(wireMockConfig().bindAddress("localhost").dynamicPort());
        server.start();
        client = Feign.builder()
                      .client(new ApacheHttpClient())
                      .encoder(encoder)
                      .decoder(decoder)
                      .contract(contract)
                      .requestInterceptor(new NaverOpenApiRequestInterceptor("id", "secret"))
                      .target(NaverOpenApiClient.class, "http://localhost:" + server.port());
    }

    @AfterEach
    void tearDown() {
        server.stop();
    }

    @Test
    void search() throws IOException {
        server.stubFor(get(urlPathEqualTo("/v1/search/local.json"))
                           .withQueryParam("display", equalTo("5"))
                           .withQueryParam("query", equalTo("곱창"))
                           .withHeader("X-Naver-Client-Id", equalTo("id"))
                           .withHeader("X-Naver-Client-Secret", equalTo("secret"))
                           .willReturn(aResponse().withStatus(200)
                                                  .withHeader("Content-Type", "application/json")
                                                  .withBody(toByteArray(resource.getInputStream()))));
        final var result = new NaverPlaceResultDTO(List.of(new ItemDTO("일번지<b>곱창</b>막창"),
                                                           new ItemDTO("양가<b>곱창</b>"),
                                                           new ItemDTO("왕십리황소<b>곱창</b> 2호점"),
                                                           new ItemDTO("청어람 망원점"),
                                                           new ItemDTO("김덕후의<b>곱창</b>조 홍대본점")));
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
