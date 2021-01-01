package com.kakaobank.codingtest.infrastructure.persistence.jpa;

import com.kakaobank.codingtest.domain.model.user.User;
import com.kakaobank.codingtest.domain.model.user.UserRepository;
import com.kakaobank.codingtest.infrastructure.persistence.jpa.entity.UserEntity;
import com.kakaobank.codingtest.infrastructure.persistence.jpa.entity.UserEntityJpaDao;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class UserRepositoryImpl implements UserRepository {
    private final UserEntityJpaDao dao;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Optional<User> findOne(final UUID id) {
        return dao.findById(id)
                  .map(this::toUser);
    }

    @Override
    public Optional<User> findOne(final String username, final String password) {
        return dao.findOneByUsername(username)
                  .filter(entity -> verifyPassword(password, entity))
                  .map(this::toUser);
    }

    private boolean verifyPassword(final String password, final UserEntity entity) {
        return passwordEncoder.matches(password, entity.getPassword());
    }

    private User toUser(final UserEntity entity) {
        return new User(entity.getId(), entity.getUsername());
    }

    @Override
    public boolean exists(final String username) {
        return dao.existsByUsername(username);
    }

    @Override
    public void store(final User user, final String password) {
        final var entity = new UserEntity();
        entity.setId(user.getId());
        entity.setUsername(user.getUsername());
        entity.setPassword(passwordEncoder.encode(password));
        dao.save(entity);
    }
}
