package com.kakaobank.codingtest.application.shared;

public interface Presenter<T extends ResponseModel, S extends ViewModel> {
    S present(T response);
}
