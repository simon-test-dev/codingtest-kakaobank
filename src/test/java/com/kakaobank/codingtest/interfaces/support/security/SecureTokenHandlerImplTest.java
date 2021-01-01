package com.kakaobank.codingtest.interfaces.support.security;

import com.kakaobank.codingtest.domain.model.user.User;
import com.kakaobank.codingtest.domain.model.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

class SecureTokenHandlerImplTest {
    private UserRepository repository;
    private SecureTokenHandlerImpl handler;

    @BeforeEach
    void setUp() {
        repository = mock(UserRepository.class);
        handler = new SecureTokenHandlerImpl(repository, "12345678901234567890123456789012");
    }

    @Test
    void testEncodeAndDecode() {
        final var id = UUID.randomUUID();
        final var user = new User(id, "username");
        doReturn(Optional.of(user)).when(repository).findOne(id);
        final var token = handler.encode(user);
        assertThat(handler.decode(token)).isEqualTo(user);
    }
}