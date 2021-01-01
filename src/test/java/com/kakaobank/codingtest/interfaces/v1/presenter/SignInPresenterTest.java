package com.kakaobank.codingtest.interfaces.v1.presenter;

import com.kakaobank.codingtest.application.user.SignInResponseModel;
import com.kakaobank.codingtest.domain.model.user.User;
import com.kakaobank.codingtest.interfaces.support.security.SecureTokenHandler;
import com.kakaobank.codingtest.interfaces.v1.viewmodel.SignInViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

class SignInPresenterTest {
    private SecureTokenHandler handler;
    private SignInPresenter presenter;

    @BeforeEach
    void setUp() {
        handler = mock(SecureTokenHandler.class);
        presenter = new SignInPresenter(handler);
    }

    @Test
    void testPresent() {
        final var user = mock(User.class);
        doReturn("token").when(handler).encode(user);
        assertThat(presenter.present(new SignInResponseModel(user))).isEqualTo(new SignInViewModel("token"));
    }
}