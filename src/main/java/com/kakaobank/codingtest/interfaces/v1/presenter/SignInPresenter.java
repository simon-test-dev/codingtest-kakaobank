package com.kakaobank.codingtest.interfaces.v1.presenter;

import com.kakaobank.codingtest.application.shared.Presenter;
import com.kakaobank.codingtest.application.user.SignInResponseModel;
import com.kakaobank.codingtest.interfaces.support.security.SecureTokenHandler;
import com.kakaobank.codingtest.interfaces.v1.viewmodel.SignInViewModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SignInPresenter implements Presenter<SignInResponseModel, SignInViewModel> {
    private final SecureTokenHandler handler;

    @Override
    public SignInViewModel present(final SignInResponseModel response) {
        final var token = handler.encode(response.getUser());
        return new SignInViewModel(token);
    }
}
