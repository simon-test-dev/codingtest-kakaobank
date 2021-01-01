package com.kakaobank.codingtest.application.user;

import com.kakaobank.codingtest.domain.model.user.UnauthorizedException;
import com.kakaobank.codingtest.domain.model.user.User;
import com.kakaobank.codingtest.domain.model.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class SignInInteractorTest {
    private UserRepository repository;
    private SignInInteractor interactor;

    @BeforeEach
    void setUp() {
        repository = mock(UserRepository.class);
        interactor = new SignInInteractor(repository);
    }

    @Test
    void testUnauthorized() {
        doReturn(Optional.empty()).when(repository).findOne("username", "password");
        assertThatThrownBy(() -> interactor.interact(new SignInRequestModel("username", "password")))
            .isExactlyInstanceOf(UnauthorizedException.class);
        verify(repository).findOne("username", "password");
    }

    @Test
    void testInteract() {
        final var user = mock(User.class);
        doReturn(Optional.of(user)).when(repository).findOne("username", "password");
        assertThat(interactor.interact(new SignInRequestModel("username", "password")))
            .isEqualTo(new SignInResponseModel(user));
        verify(repository).findOne("username", "password");
    }
}