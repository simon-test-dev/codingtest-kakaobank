package com.kakaobank.codingtest.infrastructure.persistence.jpa;

import com.kakaobank.codingtest.domain.model.user.User;
import com.kakaobank.codingtest.infrastructure.persistence.jpa.entity.UserEntity;
import com.kakaobank.codingtest.infrastructure.persistence.jpa.entity.UserEntityJpaDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.PersistenceException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doReturn;

@DataJpaTest
class UserRepositoryImplTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private UserEntityJpaDao dao;
    @MockBean
    private PasswordEncoder passwordEncoder;
    private UserRepositoryImpl repository;
    private UserEntity entity;

    @BeforeEach
    void setUp() {
        repository = new UserRepositoryImpl(dao, passwordEncoder);
        entity = new UserEntity();
        entity.setId(UUID.randomUUID());
        entity.setUsername("username");
        entity.setPassword("password");
        entityManager.persistAndFlush(entity);
    }

    @Test
    void testFindOneById() {
        assertThat(repository.findOne(UUID.randomUUID())).isEmpty();
        assertThat(repository.findOne(entity.getId())).contains(new User(entity.getId(), entity.getUsername()));
    }

    @Test
    void testFindOneByUsernameAndPassword() {
        doReturn(true).when(passwordEncoder).matches("password", "password");
        assertThat(repository.findOne("not_exists", "password")).isEmpty();
        assertThat(repository.findOne("username", "not_matched")).isEmpty();
        assertThat(repository.findOne("username", "password")).contains(new User(entity.getId(), entity.getUsername()));
    }

    @Test
    void testExists() {
        assertThat(repository.exists("not_exists")).isFalse();
        assertThat(repository.exists("username")).isTrue();
    }

    @Test
    void testStore() {
        doReturn("password").when(passwordEncoder).encode("password");
        repository.store(new User(UUID.randomUUID(), "another"), "password");
    }

    @Test
    void testDuplicateUsername() {
        doReturn("password").when(passwordEncoder).encode("password");
        assertThatThrownBy(() -> {
            repository.store(new User(UUID.randomUUID(), "username"), "password");
            entityManager.flush();
        }).isExactlyInstanceOf(PersistenceException.class);
    }
}