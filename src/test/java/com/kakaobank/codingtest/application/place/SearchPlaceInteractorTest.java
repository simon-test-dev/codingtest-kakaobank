package com.kakaobank.codingtest.application.place;

import com.kakaobank.codingtest.domain.model.place.PlaceSearchHandler;
import com.kakaobank.codingtest.domain.model.place.PlaceSearchReportHandler;
import com.kakaobank.codingtest.domain.model.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class SearchPlaceInteractorTest {
    private PlaceSearchHandler primary;
    private PlaceSearchHandler secondary;
    private PlaceSearchReportHandler reportHandler;
    private SearchPlaceInteractor interactor;
    private User user;
    private SearchPlaceRequestModel request;

    @BeforeEach
    void setUp() {
        primary = mock(PlaceSearchHandler.class);
        secondary = mock(PlaceSearchHandler.class);
        reportHandler = mock(PlaceSearchReportHandler.class);
        interactor = new SearchPlaceInteractor(primary, secondary, reportHandler);
        user = mock(User.class);
        request = new SearchPlaceRequestModel(user, "은행");
    }

    @DisplayName("open api 가 모두 장애일 경우")
    @Test
    void testProblemBoth() {
        doReturn(Optional.empty()).when(primary).search("은행");
        doReturn(Optional.empty()).when(secondary).search("은행");
        assertThatThrownBy(() -> interactor.interact(request))
            .isExactlyInstanceOf(SearchPlaceUnavailableException.class);
        verify(reportHandler).logging(user, "은행");
    }

    @DisplayName("open api 가 중 kakao 장애일 경우")
    @Test
    void testProblemKakao() {
        doReturn(Optional.empty()).when(primary).search("은행");
        doReturn(Optional.of(List.of("K뱅크"))).when(secondary).search("은행");
        assertThat(interactor.interact(request))
            .isEqualTo(new SearchPlaceResponseModel(List.of("K뱅크")));
        verify(reportHandler).logging(user, "은행");
    }

    @DisplayName("open api 가 중 naver 장애일 경우")
    @Test
    void testProblemNaver() {
        doReturn(Optional.of((List.of("카카오뱅크")))).when(primary).search("은행");
        doReturn(Optional.empty()).when(secondary).search("은행");
        assertThat(interactor.interact(request))
            .isEqualTo(new SearchPlaceResponseModel(List.of("카카오뱅크")));
        verify(reportHandler).logging(user, "은행");
    }

    @DisplayName("곱창 검색결과")
    @Test
    void testRestaurant() {
        request = new SearchPlaceRequestModel(user, "곱창");
        doReturn(Optional.of((List.of("A곱창", "B곱창", "C곱창", "D곱창")))).when(primary).search("곱창");
        doReturn(Optional.of((List.of("A곱창", "E곱창", "D곱창", "C곱창")))).when(secondary).search("곱창");
        assertThat(interactor.interact(request))
            .isEqualTo(new SearchPlaceResponseModel(List.of("A곱창", "C곱창", "D곱창", "B곱창", "E곱창")));
        verify(reportHandler).logging(user, "곱창");
    }

    @DisplayName("은행 검색결과")
    @Test
    void testBank() {
        doReturn(Optional.of((List.of("카카오뱅크", "우리은행", "국민은행", "부산은행", "새마을금고")))).when(primary).search("은행");
        doReturn(Optional.of((List.of("카카오뱅크", "부산은행", "하나은행", "국민은행", "기업은행")))).when(secondary).search("은행");
        assertThat(interactor.interact(request))
            .isEqualTo(new SearchPlaceResponseModel(List.of("카카오뱅크", "국민은행", "부산은행", "우리은행", "새마을금고", "하나은행", "기업은행")));
        verify(reportHandler).logging(user, "은행");
    }
}