package com.kakaobank.codingtest.interfaces.v1.presenter;

import com.kakaobank.codingtest.application.place.SearchPlaceResponseModel;
import com.kakaobank.codingtest.application.shared.Presenter;
import com.kakaobank.codingtest.interfaces.v1.viewmodel.SearchPlaceViewModel;
import org.springframework.stereotype.Component;

import static java.util.stream.Collectors.toList;

@Component
public class SearchPlacePresenter implements Presenter<SearchPlaceResponseModel, SearchPlaceViewModel> {
    @Override
    public SearchPlaceViewModel present(final SearchPlaceResponseModel response) {
        final var places = response.getPlaces()
                                   .stream()
                                   .map(SearchPlaceViewModel.PlaceDTO::new)
                                   .collect(toList());
        return new SearchPlaceViewModel(places);
    }
}
