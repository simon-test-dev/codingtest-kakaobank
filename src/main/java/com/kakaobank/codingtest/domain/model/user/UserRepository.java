package com.kakaobank.codingtest.domain.model.user;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    Optional<User> findOne(UUID id);

    Optional<User> findOne(String username, String password);

    boolean exists(String username);

    void store(User user, String password);
}
