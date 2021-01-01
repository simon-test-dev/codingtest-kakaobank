package com.kakaobank.codingtest.application.place;

import com.kakaobank.codingtest.application.shared.RequestModel;
import com.kakaobank.codingtest.domain.model.user.User;
import lombok.Data;

@Data
public class SearchPlaceRequestModel implements RequestModel {
    private final User user;
    private final String keyword;
}
