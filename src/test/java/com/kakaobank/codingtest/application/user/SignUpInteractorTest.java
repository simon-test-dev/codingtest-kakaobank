package com.kakaobank.codingtest.application.user;

import com.kakaobank.codingtest.domain.model.user.User;
import com.kakaobank.codingtest.domain.model.user.UserFactory;
import com.kakaobank.codingtest.domain.model.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class SignUpInteractorTest {
    private UserFactory factory;
    private UserRepository repository;
    private SignUpInteractor interactor;

    @BeforeEach
    void setUp() {
        factory = mock(UserFactory.class);
        repository = mock(UserRepository.class);
        interactor = new SignUpInteractor(factory, repository);
    }

    @Test
    void testInteract() {
        final var user = mock(User.class);
        doReturn(user).when(factory).create("username");
        assertThat(interactor.interact(new SignUpRequestModel("username", "password")))
            .isEqualTo(new SignUpResponseModel(user));
        verify(repository).store(user, "password");
    }
}