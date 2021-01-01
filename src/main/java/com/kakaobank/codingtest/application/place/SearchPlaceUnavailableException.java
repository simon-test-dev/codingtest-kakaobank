package com.kakaobank.codingtest.application.place;

import com.kakaobank.codingtest.domain.support.AbstractServerKakaobankException;

import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;

public class SearchPlaceUnavailableException extends AbstractServerKakaobankException {
    private static final long serialVersionUID = -934823285678106020L;

    public SearchPlaceUnavailableException() {
        super(SERVICE_UNAVAILABLE);
    }
}
