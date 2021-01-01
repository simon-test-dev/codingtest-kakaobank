package com.kakaobank.codingtest.domain.model.user;

import com.kakaobank.codingtest.domain.shared.Factory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserFactory implements Factory {
    private final UserRepository repository;

    public User create(final String username) {
        if (repository.exists(username)) {
            throw new ExistsUsernameException(username);
        }
        return new User(UUID.randomUUID(), username);
    }
}
