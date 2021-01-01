package com.kakaobank.codingtest.domain.model.place;

import com.kakaobank.codingtest.domain.shared.ValueObject;
import lombok.Data;

@Data
public class SearchReport implements ValueObject {
    private static final long serialVersionUID = 6236156225517114262L;
    private final String keyword;
    private final Long count;
}
