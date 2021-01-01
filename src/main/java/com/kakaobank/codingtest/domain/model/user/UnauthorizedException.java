package com.kakaobank.codingtest.domain.model.user;

import com.kakaobank.codingtest.domain.support.AbstractClientKakaobankException;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public class UnauthorizedException extends AbstractClientKakaobankException {
    private static final long serialVersionUID = -9126664948201406628L;

    public UnauthorizedException() {
        super(UNAUTHORIZED);
    }

    public UnauthorizedException(final String reason) {
        super(UNAUTHORIZED, reason);
    }

    public UnauthorizedException(final Throwable cause) {
        super(UNAUTHORIZED, cause.getMessage(), cause);
    }
}
