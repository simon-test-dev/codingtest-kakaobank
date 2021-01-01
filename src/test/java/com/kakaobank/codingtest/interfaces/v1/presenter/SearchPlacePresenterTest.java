package com.kakaobank.codingtest.interfaces.v1.presenter;

import com.kakaobank.codingtest.application.place.SearchPlaceResponseModel;
import com.kakaobank.codingtest.interfaces.v1.viewmodel.SearchPlaceViewModel;
import com.kakaobank.codingtest.interfaces.v1.viewmodel.SearchPlaceViewModel.PlaceDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SearchPlacePresenterTest {
    private SearchPlacePresenter presenter;

    @BeforeEach
    void setUp() {
        presenter = new SearchPlacePresenter();
    }

    @Test
    void testPresent() {
        assertThat(presenter.present(new SearchPlaceResponseModel(List.of("place1", "place2"))))
            .isEqualTo(new SearchPlaceViewModel(List.of(new PlaceDTO("place1"), new PlaceDTO("place2"))));
    }
}