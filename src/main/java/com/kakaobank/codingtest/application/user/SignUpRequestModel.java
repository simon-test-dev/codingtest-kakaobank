package com.kakaobank.codingtest.application.user;

import com.kakaobank.codingtest.application.shared.RequestModel;
import lombok.Data;

@Data
public class SignUpRequestModel implements RequestModel {
    private final String username;
    private final String password;
}
