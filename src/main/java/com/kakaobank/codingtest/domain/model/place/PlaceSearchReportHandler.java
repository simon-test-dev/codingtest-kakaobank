package com.kakaobank.codingtest.domain.model.place;

import com.kakaobank.codingtest.domain.model.user.User;
import com.kakaobank.codingtest.domain.shared.DomainService;

import java.util.List;

public interface PlaceSearchReportHandler extends DomainService {
    void logging(User user, String keyword);

    List<SearchHistory> history(User user);

    List<SearchReport> popularKeywords();
}
