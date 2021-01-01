package com.kakaobank.codingtest.application.shared;

import com.kakaobank.codingtest.domain.shared.ApplicationService;

public interface Interactor<T extends RequestModel, S extends ResponseModel> extends ApplicationService {
    S interact(T request);
}
