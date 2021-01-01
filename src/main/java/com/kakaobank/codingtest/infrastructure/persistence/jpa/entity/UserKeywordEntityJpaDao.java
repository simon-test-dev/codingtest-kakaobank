package com.kakaobank.codingtest.infrastructure.persistence.jpa.entity;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface UserKeywordEntityJpaDao extends JpaRepository<UserKeywordEntity, Long> {
    List<UserKeywordEntity> findAllByUserId(UUID userId, Pageable pageable);

    @Query("SELECT u.keyword AS keyword, COUNT(u) AS count FROM UserKeywordEntity u GROUP BY u.keyword ORDER BY COUNT(u.keyword) DESC")
    List<SearchReportDTO> popularKeywords(Pageable pageable);
}
