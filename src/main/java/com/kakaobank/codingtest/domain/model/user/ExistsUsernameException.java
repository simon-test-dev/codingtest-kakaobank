package com.kakaobank.codingtest.domain.model.user;

import com.kakaobank.codingtest.domain.support.AbstractClientKakaobankException;

import static org.springframework.http.HttpStatus.CONFLICT;

public class ExistsUsernameException extends AbstractClientKakaobankException {
    private static final long serialVersionUID = 118298404265815436L;

    public ExistsUsernameException(final String username) {
        super(CONFLICT, "Already exists username:`" + username + "`");
    }
}
