package com.kakaobank.codingtest.domain.support;

import org.springframework.http.HttpStatus;

public class AbstractServerKakaobankException extends AbstractKakaobankException {

    private static final long serialVersionUID = 1373899006314942662L;

    public AbstractServerKakaobankException(final HttpStatus status) {
        super(status);
    }

    public AbstractServerKakaobankException(final HttpStatus status, final String reason) {
        super(status, reason);
    }

    public AbstractServerKakaobankException(final HttpStatus status, final String reason, final Throwable cause) {
        super(status, reason, cause);
    }
}
