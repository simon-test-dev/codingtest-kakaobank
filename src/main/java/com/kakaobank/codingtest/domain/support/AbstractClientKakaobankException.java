package com.kakaobank.codingtest.domain.support;

import org.springframework.http.HttpStatus;

public class AbstractClientKakaobankException extends AbstractKakaobankException {
    private static final long serialVersionUID = -7963823143200349672L;

    public AbstractClientKakaobankException(final HttpStatus status) {
        super(status);
    }

    public AbstractClientKakaobankException(final HttpStatus status, final String reason) {
        super(status, reason);
    }

    public AbstractClientKakaobankException(final HttpStatus status, final String reason, final Throwable cause) {
        super(status, reason, cause);
    }
}
