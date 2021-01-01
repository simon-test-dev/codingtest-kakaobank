package com.kakaobank.codingtest.domain.support;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class AbstractKakaobankException extends ResponseStatusException {
    private static final long serialVersionUID = -4828320259823498016L;

    public AbstractKakaobankException(final HttpStatus status) {
        super(status);
    }

    public AbstractKakaobankException(final HttpStatus status, final String reason) {
        super(status, reason);
    }

    public AbstractKakaobankException(final HttpStatus status, final String reason, final Throwable cause) {
        super(status, reason, cause);
    }

}
