package com.kakaobank.codingtest.infrastructure.persistence.jpa.entity;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "user_keywords", indexes = {@Index(columnList = "userId,createdAt DESC"),
                                          @Index(columnList = "keyword")})
public class UserKeywordEntity {
    @Id
    @GeneratedValue
    private Long id;
    @Column(updatable = false, nullable = false)
    private UUID userId;
    @Column(length = 200, updatable = false, nullable = false)
    private String keyword;
    @Column(nullable = false, columnDefinition = "DATETIME")
    private LocalDateTime createdAt;
}
