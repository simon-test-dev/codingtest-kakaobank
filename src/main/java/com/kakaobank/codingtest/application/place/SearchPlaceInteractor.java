package com.kakaobank.codingtest.application.place;

import com.kakaobank.codingtest.application.shared.Interactor;
import com.kakaobank.codingtest.domain.model.place.PlaceSearchHandler;
import com.kakaobank.codingtest.domain.model.place.PlaceSearchReportHandler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;

@Service
public class SearchPlaceInteractor implements Interactor<SearchPlaceRequestModel, SearchPlaceResponseModel> {
    private final PlaceSearchHandler primary;
    private final PlaceSearchHandler secondary;
    private final PlaceSearchReportHandler reportHandler;

    public SearchPlaceInteractor(@Qualifier("placeSearchHandlerKakaoImpl") final PlaceSearchHandler primary,
                                 @Qualifier("placeSearchHandlerNaverImpl") final PlaceSearchHandler secondary,
                                 final PlaceSearchReportHandler reportHandler) {
        this.primary = primary;
        this.secondary = secondary;
        this.reportHandler = reportHandler;
    }

    @Override
    public SearchPlaceResponseModel interact(final SearchPlaceRequestModel request) {
        reportHandler.logging(request.getUser(), request.getKeyword());
        final var first = primary.search(request.getKeyword());
        if (first.isEmpty()) {
            return secondary.search(request.getKeyword())
                            .map(SearchPlaceResponseModel::new)
                            .orElseThrow(SearchPlaceUnavailableException::new);
        }
        return secondary.search(request.getKeyword())
                        .map(second -> rearrange(first.get(), second))
                        .map(SearchPlaceResponseModel::new)
                        .orElseGet(() -> new SearchPlaceResponseModel(first.get()));
    }

    private List<String> rearrange(final List<String> first, final List<String> second) {
        return concat(sort(first, new HashSet<>(second)),
                      exclude(second, new HashSet<>(first))).collect(toList());
    }

    private Stream<String> sort(final List<String> first, final Set<String> second) {
        final var intersection = first.stream()
                                      .filter(second::contains);
        return concat(intersection, exclude(first, second));
    }

    private Stream<String> exclude(final List<String> origin, final Set<String> exclude) {
        return origin.stream()
                     .filter(not(exclude::contains));
    }
}
