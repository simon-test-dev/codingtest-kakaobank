package com.kakaobank.codingtest.interfaces.v1.viewmodel;

import com.kakaobank.codingtest.application.shared.ViewModel;
import lombok.Data;

import java.util.List;

@Data
public class SearchPlaceViewModel implements ViewModel {
    private final List<PlaceDTO> places;

    @Data
    public static class PlaceDTO {
        private final String name;
    }
}
