package com.kakaobank.codingtest.infrastructure.rest.feign.naver;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NaverPlaceResultDTO {
    private List<ItemDTO> items = List.of();

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class ItemDTO {
        private String title;
    }
}
