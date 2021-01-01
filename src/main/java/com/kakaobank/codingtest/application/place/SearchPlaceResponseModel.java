package com.kakaobank.codingtest.application.place;

import com.kakaobank.codingtest.application.shared.ResponseModel;
import lombok.Data;

import java.util.List;

@Data
public class SearchPlaceResponseModel implements ResponseModel {
    private final List<String> places;
}
