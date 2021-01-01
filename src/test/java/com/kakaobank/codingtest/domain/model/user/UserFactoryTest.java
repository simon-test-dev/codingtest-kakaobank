package com.kakaobank.codingtest.domain.model.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class UserFactoryTest {
    private UserRepository repository;
    private UserFactory factory;

    @BeforeEach
    void setUp() {
        repository = mock(UserRepository.class);
        factory = new UserFactory(repository);
    }

    @Test
    void testExistUser() {
        doReturn(true).when(repository).exists("user");
        assertThatThrownBy(() -> factory.create("user")).isInstanceOf(ExistsUsernameException.class);
        verify(repository).exists("user");
    }

    @Test
    void testCreate() {
        doReturn(false).when(repository).exists("user");
        assertThat(factory.create("user")).matches(user -> user.getId() != null)
                                          .matches(user -> "user".equals(user.getUsername()));
        verify(repository).exists("user");
    }
}