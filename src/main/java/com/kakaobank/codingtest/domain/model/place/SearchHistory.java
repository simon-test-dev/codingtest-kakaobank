package com.kakaobank.codingtest.domain.model.place;

import com.kakaobank.codingtest.domain.shared.ValueObject;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SearchHistory implements ValueObject {
    private static final long serialVersionUID = -1113730963610078601L;
    private final String keyword;
    private final LocalDateTime createdAt;
}
