package com.kakaobank.codingtest.interfaces.v1.controller;

import com.kakaobank.codingtest.application.place.SearchPlaceRequestModel;
import com.kakaobank.codingtest.application.place.SearchPlaceResponseModel;
import com.kakaobank.codingtest.application.shared.Interactor;
import com.kakaobank.codingtest.application.shared.Presenter;
import com.kakaobank.codingtest.domain.model.place.PlaceSearchReportHandler;
import com.kakaobank.codingtest.domain.model.place.SearchReport;
import com.kakaobank.codingtest.domain.model.user.User;
import com.kakaobank.codingtest.interfaces.support.resolver.AuthenticatedUserMethodArgumentResolver;
import com.kakaobank.codingtest.interfaces.support.security.SecureTokenHandler;
import com.kakaobank.codingtest.interfaces.v1.viewmodel.SearchPlaceViewModel;
import com.kakaobank.codingtest.interfaces.v1.viewmodel.SearchPlaceViewModel.PlaceDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PlacesControllerTest {
    private SecureTokenHandler secureTokenHandler;
    private PlaceSearchReportHandler reportHandler;
    private Interactor<SearchPlaceRequestModel, SearchPlaceResponseModel> interactor;
    private Presenter<SearchPlaceResponseModel, SearchPlaceViewModel> presenter;
    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        secureTokenHandler = mock(SecureTokenHandler.class);
        reportHandler = mock(PlaceSearchReportHandler.class);
        interactor = mock(Interactor.class);
        presenter = mock(Presenter.class);
        mvc = MockMvcBuilders.standaloneSetup(new PlacesController(reportHandler, interactor, presenter))
                             .setCustomArgumentResolvers(new AuthenticatedUserMethodArgumentResolver(secureTokenHandler))
                             .build();
    }

    @Test
    void testSearch() throws Exception {
        final var user = mock(User.class);
        doReturn(user).when(secureTokenHandler).decode("token");
        final var response = mock(SearchPlaceResponseModel.class);
        doReturn(response).when(interactor).interact(new SearchPlaceRequestModel(user, "keyword"));
        doReturn(new SearchPlaceViewModel(List.of(new PlaceDTO("keyword1"),
                                                  new PlaceDTO("keyword2")))).when(presenter).present(response);

        mvc.perform(get("/v1/places/search").param("keyword", "keyword")
                                            .header("Authentication", "Bearer token"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.places[0].name", is("keyword1")))
           .andExpect(jsonPath("$.places[1].name", is("keyword2")));
    }

    @Test
    void testPoplarKeywords() throws Exception {
        doReturn(List.of(new SearchReport("keyword1", 2L),
                         new SearchReport("keyword2", 1L))).when(reportHandler).popularKeywords();
        mvc.perform(get("/v1/places/popular-keywords"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.keywords[0].keyword", is("keyword1")))
           .andExpect(jsonPath("$.keywords[0].count", is(2)))
           .andExpect(jsonPath("$.keywords[1].keyword", is("keyword2")))
           .andExpect(jsonPath("$.keywords[1].count", is(1)));
    }
}