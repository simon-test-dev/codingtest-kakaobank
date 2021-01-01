package com.kakaobank.codingtest.infrastructure.persistence.jpa.entity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserEntityJpaDao extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findOneByUsername(String username);

    boolean existsByUsername(String username);
}
