package com.kakaobank.codingtest.interfaces.support.security;

import com.kakaobank.codingtest.domain.model.user.User;

public interface SecureTokenHandler {
    String encode(User user);

    User decode(String token);
}
