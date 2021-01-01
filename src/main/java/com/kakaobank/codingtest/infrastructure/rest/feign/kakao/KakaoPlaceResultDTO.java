package com.kakaobank.codingtest.infrastructure.rest.feign.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class KakaoPlaceResultDTO {
    private List<DocumentDTO> documents = List.of();

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class DocumentDTO {
        @JsonProperty("place_name")
        private String name;
    }
}
