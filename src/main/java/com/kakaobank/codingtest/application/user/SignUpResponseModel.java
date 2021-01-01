package com.kakaobank.codingtest.application.user;

import com.kakaobank.codingtest.application.shared.ResponseModel;
import com.kakaobank.codingtest.domain.model.user.User;
import lombok.Data;

@Data
public class SignUpResponseModel implements ResponseModel {
    private final User user;
}
