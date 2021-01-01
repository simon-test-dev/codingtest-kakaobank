package com.kakaobank.codingtest.infrastructure.persistence.jpa;

import com.kakaobank.codingtest.domain.model.place.PlaceSearchReportHandler;
import com.kakaobank.codingtest.domain.model.place.SearchHistory;
import com.kakaobank.codingtest.domain.model.place.SearchReport;
import com.kakaobank.codingtest.domain.model.user.User;
import com.kakaobank.codingtest.infrastructure.persistence.jpa.entity.SearchReportDTO;
import com.kakaobank.codingtest.infrastructure.persistence.jpa.entity.UserKeywordEntity;
import com.kakaobank.codingtest.infrastructure.persistence.jpa.entity.UserKeywordEntityJpaDao;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.time.LocalDateTime.now;
import static java.util.stream.Collectors.toList;
import static org.springframework.data.domain.PageRequest.of;
import static org.springframework.data.domain.Sort.Direction.DESC;
import static org.springframework.data.domain.Sort.by;
import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;
import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

@RequiredArgsConstructor
@Service
public class PlaceSearchReportHandlerImpl implements PlaceSearchReportHandler {
    private static final PageRequest DEFAULT_HISTORY_PAGE = of(0, 10, by(DESC, "createdAt"));
    private static final PageRequest DEFAULT_POPULAR_KEYWORDS_PAGE = of(0, 10);
    private final UserKeywordEntityJpaDao dao;

    @Transactional(propagation = REQUIRES_NEW, isolation = SERIALIZABLE)
    @Override
    public void logging(final User user, final String keyword) {
        dao.save(toKeyword(user, keyword));
    }


    private UserKeywordEntity toKeyword(final User user, final String keyword) {
        final var entity = new UserKeywordEntity();
        entity.setUserId(user.getId());
        entity.setKeyword(keyword);
        entity.setCreatedAt(now());
        return entity;
    }

    @Override
    public List<SearchHistory> history(final User user) {
        return dao.findAllByUserId(user.getId(), DEFAULT_HISTORY_PAGE)
                  .stream()
                  .map(this::toHistory)
                  .collect(toList());
    }

    private SearchHistory toHistory(final UserKeywordEntity history) {
        return new SearchHistory(history.getKeyword(),
                                 history.getCreatedAt());
    }

    @Cacheable(cacheNames = "report", key = "'popularKeywords'", sync = true)
    @Override
    public List<SearchReport> popularKeywords() {
        return dao.popularKeywords(DEFAULT_POPULAR_KEYWORDS_PAGE)
                  .stream()
                  .map(this::toReport)
                  .collect(toList());
    }

    private SearchReport toReport(final SearchReportDTO report) {
        return new SearchReport(report.getKeyword(), report.getCount());
    }
}
