package com.kakaobank.codingtest.interfaces.v1.viewmodel;

import com.kakaobank.codingtest.application.shared.ViewModel;
import lombok.Data;

@Data
public class SignInViewModel implements ViewModel {
    private final String accessToken;
}
