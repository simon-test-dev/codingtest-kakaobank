package com.kakaobank.codingtest.interfaces.v1.controller;

import com.kakaobank.codingtest.application.place.SearchPlaceRequestModel;
import com.kakaobank.codingtest.application.place.SearchPlaceResponseModel;
import com.kakaobank.codingtest.application.shared.Interactor;
import com.kakaobank.codingtest.application.shared.Presenter;
import com.kakaobank.codingtest.domain.model.place.PlaceSearchReportHandler;
import com.kakaobank.codingtest.domain.model.user.User;
import com.kakaobank.codingtest.interfaces.v1.viewmodel.PopularKeywordViewModel;
import com.kakaobank.codingtest.interfaces.v1.viewmodel.SearchPlaceViewModel;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/places")
public class PlacesController {
    private final PlaceSearchReportHandler reportHandler;
    private final Interactor<SearchPlaceRequestModel, SearchPlaceResponseModel> interactor;
    private final Presenter<SearchPlaceResponseModel, SearchPlaceViewModel> presenter;

    @GetMapping("search")
    public SearchPlaceViewModel search(@RequestParam("keyword") final String keyword,
                                       final User user) {
        final var response = interactor.interact(new SearchPlaceRequestModel(user, keyword));
        return presenter.present(response);
    }

    @GetMapping("popular-keywords")
    public PopularKeywordViewModel poplarKeywords() {
        return new PopularKeywordViewModel(reportHandler.popularKeywords());
    }
}
