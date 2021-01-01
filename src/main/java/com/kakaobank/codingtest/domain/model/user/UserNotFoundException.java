package com.kakaobank.codingtest.domain.model.user;

import com.kakaobank.codingtest.domain.support.AbstractClientKakaobankException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

public class UserNotFoundException extends AbstractClientKakaobankException {
    private static final long serialVersionUID = 5772077654418928327L;

    public UserNotFoundException() {
        super(NOT_FOUND);
    }
}
