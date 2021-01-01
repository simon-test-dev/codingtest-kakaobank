package com.kakaobank.codingtest.interfaces.v1.viewmodel;

import com.kakaobank.codingtest.application.shared.ViewModel;
import com.kakaobank.codingtest.domain.model.place.SearchHistory;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Data
public class SearchHistoryViewModel implements ViewModel {
    private final List<HistoryDTO> histories;

    public SearchHistoryViewModel(final List<SearchHistory> histories) {
        this.histories = histories.stream()
                                  .map(history -> new HistoryDTO(history.getKeyword(), history.getCreatedAt()))
                                  .collect(toList());
    }

    @Data
    public static class HistoryDTO {
        private final String keyword;
        private final LocalDateTime createdAt;
    }
}
