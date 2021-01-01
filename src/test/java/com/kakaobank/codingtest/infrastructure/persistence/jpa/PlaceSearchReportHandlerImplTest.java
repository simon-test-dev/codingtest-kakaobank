package com.kakaobank.codingtest.infrastructure.persistence.jpa;

import com.kakaobank.codingtest.domain.model.place.SearchHistory;
import com.kakaobank.codingtest.domain.model.place.SearchReport;
import com.kakaobank.codingtest.domain.model.user.User;
import com.kakaobank.codingtest.infrastructure.persistence.jpa.entity.UserKeywordEntity;
import com.kakaobank.codingtest.infrastructure.persistence.jpa.entity.UserKeywordEntityJpaDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.UUID;

import static java.time.LocalDateTime.now;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PlaceSearchReportHandlerImplTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private UserKeywordEntityJpaDao dao;
    private PlaceSearchReportHandlerImpl handler;
    private UUID id;

    @BeforeEach
    void setUp() {
        handler = new PlaceSearchReportHandlerImpl(dao);
        id = randomUUID();
    }

    @Test
    void testLogging() {
        handler.logging(new User(id, "username"), "keyword1");
        handler.logging(new User(id, "username"), "keyword2");
        handler.logging(new User(id, "username"), "keyword2");
        assertThat(entityManager.getEntityManager()
                                .createQuery("SELECT COUNT(p) FROM UserKeywordEntity p ")
                                .getSingleResult()).isEqualTo(3L);
    }

    @Test
    void testHistory() {
        final var keyword1 = toKeyword(id, "keyword1");
        entityManager.persist(keyword1);
        final var keyword2 = toKeyword(id, "keyword2");
        entityManager.persist(keyword2);
        final var keyword3 = toKeyword(randomUUID(), "keyword3");
        entityManager.persist(keyword3);
        assertThat(handler.history(new User(id, "username")))
            .containsExactly(new SearchHistory(keyword2.getKeyword(), keyword2.getCreatedAt()),
                             new SearchHistory(keyword1.getKeyword(), keyword1.getCreatedAt()));

    }

    private UserKeywordEntity toKeyword(final UUID userId, final String keyword) {
        final var entity = new UserKeywordEntity();
        entity.setUserId(userId);
        entity.setKeyword(keyword);
        entity.setCreatedAt(now());
        return entity;
    }

    @Test
    void testPopularKeywords() {
        entityManager.persist(toKeyword(id, "keyword1"));
        entityManager.persist(toKeyword(id, "keyword2"));
        entityManager.persist(toKeyword(randomUUID(), "keyword2"));
        assertThat(handler.popularKeywords())
            .containsExactly(new SearchReport("keyword2", 2L),
                             new SearchReport("keyword1", 1L));
    }
}