package com.kakaobank.codingtest.interfaces.v1.viewmodel;

import com.kakaobank.codingtest.application.shared.ViewModel;
import com.kakaobank.codingtest.domain.model.place.SearchReport;
import lombok.Data;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Data
public class PopularKeywordViewModel implements ViewModel {
    private final List<KeywordCountDTO> keywords;

    public PopularKeywordViewModel(final List<SearchReport> keywords) {
        this.keywords = keywords.stream()
                                .map(report -> new KeywordCountDTO(report.getKeyword(), report.getCount()))
                                .collect(toList());
    }

    @Data
    public static class KeywordCountDTO {
        private final String keyword;
        private final Long count;
    }
}
