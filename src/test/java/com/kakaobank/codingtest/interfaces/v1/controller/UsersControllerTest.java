package com.kakaobank.codingtest.interfaces.v1.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kakaobank.codingtest.application.shared.Interactor;
import com.kakaobank.codingtest.application.shared.Presenter;
import com.kakaobank.codingtest.application.user.SignInRequestModel;
import com.kakaobank.codingtest.application.user.SignInResponseModel;
import com.kakaobank.codingtest.application.user.SignUpRequestModel;
import com.kakaobank.codingtest.application.user.SignUpResponseModel;
import com.kakaobank.codingtest.domain.model.place.PlaceSearchReportHandler;
import com.kakaobank.codingtest.domain.model.place.SearchHistory;
import com.kakaobank.codingtest.domain.model.user.User;
import com.kakaobank.codingtest.interfaces.support.resolver.AuthenticatedUserMethodArgumentResolver;
import com.kakaobank.codingtest.interfaces.support.security.SecureTokenHandler;
import com.kakaobank.codingtest.interfaces.v1.viewmodel.SignInViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;
import static java.time.LocalDateTime.now;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UsersControllerTest {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().registerModule(new JavaTimeModule())
                                                                        .configure(WRITE_DATES_AS_TIMESTAMPS, false);
    private SecureTokenHandler secureTokenHandler;
    private Interactor<SignUpRequestModel, SignUpResponseModel> signUpInteractor;
    private Interactor<SignInRequestModel, SignInResponseModel> signInInteractor;
    private Presenter<SignInResponseModel, SignInViewModel> signInPresenter;
    private PlaceSearchReportHandler reportHandler;
    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        secureTokenHandler = mock(SecureTokenHandler.class);
        signUpInteractor = mock(Interactor.class);
        signInInteractor = mock(Interactor.class);
        signInPresenter = mock(Presenter.class);
        reportHandler = mock(PlaceSearchReportHandler.class);
        mvc = MockMvcBuilders.standaloneSetup(new UsersController(signUpInteractor,
                                                                  signInInteractor,
                                                                  signInPresenter,
                                                                  reportHandler))
                             .setMessageConverters(new MappingJackson2HttpMessageConverter(OBJECT_MAPPER))
                             .setCustomArgumentResolvers(new AuthenticatedUserMethodArgumentResolver(secureTokenHandler))
                             .build();
    }

    @Test
    void testSignUp() throws Exception {
        mvc.perform(post("/v1/users/sign-up").contentType(APPLICATION_JSON)
                                             // language=JSON
                                             .content("{\n" +
                                                      "  \"username\": \"test\",\n" +
                                                      "  \"password\": \"test\"\n" +
                                                      "}"))

           .andExpect(status().isCreated());
    }

    @Test
    void testSignIn() throws Exception {
        final var response = mock(SignInResponseModel.class);
        doReturn(response).when(signInInteractor).interact(new SignInRequestModel("test", "test"));
        doReturn(new SignInViewModel("token")).when(signInPresenter).present(response);
        mvc.perform(post("/v1/users/sign-in").contentType(APPLICATION_JSON)
                                             // language=JSON
                                             .content("{\n" +
                                                      "  \"username\": \"test\",\n" +
                                                      "  \"password\": \"test\"\n" +
                                                      "}"))

           .andExpect(status().isOk())
           .andExpect(jsonPath("$.accessToken", is("token")));
    }

    @Test
    void testSearchHistory() throws Exception {
        final var user = mock(User.class);
        doReturn(user).when(secureTokenHandler).decode("token");
        final var keyword1 = new SearchHistory("keyword1", now());
        final var keyword2 = new SearchHistory("keyword2", now());
        doReturn(List.of(keyword1, keyword2)).when(reportHandler).history(user);
        mvc.perform(get("/v1/users/histories/search").header("Authentication", "Bearer token"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.histories[0].keyword", is("keyword1")))
           .andExpect(jsonPath("$.histories[0].createdAt", is(keyword1.getCreatedAt().format(ISO_LOCAL_DATE_TIME))))
           .andExpect(jsonPath("$.histories[1].keyword", is("keyword2")))
           .andExpect(jsonPath("$.histories[1].createdAt", is(keyword2.getCreatedAt().format(ISO_LOCAL_DATE_TIME))));
    }
}