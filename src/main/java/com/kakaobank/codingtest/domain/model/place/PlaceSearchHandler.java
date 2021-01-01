package com.kakaobank.codingtest.domain.model.place;

import com.kakaobank.codingtest.domain.shared.DomainService;

import java.util.List;
import java.util.Optional;

public interface PlaceSearchHandler extends DomainService {
    Optional<List<String>> search(String keyword);
}
