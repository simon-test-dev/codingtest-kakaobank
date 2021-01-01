package com.kakaobank.codingtest.infrastructure.persistence.jpa.entity;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Data
@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    private UUID id;
    @Column(length = 50, unique = true, updatable = false, nullable = false)
    private String username;
    @Column(length = 200, nullable = false)
    private String password;
}
