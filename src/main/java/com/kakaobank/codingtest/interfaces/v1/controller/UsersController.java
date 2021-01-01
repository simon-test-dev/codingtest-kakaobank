package com.kakaobank.codingtest.interfaces.v1.controller;

import com.kakaobank.codingtest.application.shared.Interactor;
import com.kakaobank.codingtest.application.shared.Presenter;
import com.kakaobank.codingtest.application.user.SignInRequestModel;
import com.kakaobank.codingtest.application.user.SignInResponseModel;
import com.kakaobank.codingtest.application.user.SignUpRequestModel;
import com.kakaobank.codingtest.application.user.SignUpResponseModel;
import com.kakaobank.codingtest.domain.model.place.PlaceSearchReportHandler;
import com.kakaobank.codingtest.domain.model.user.User;
import com.kakaobank.codingtest.interfaces.v1.command.SignInCommand;
import com.kakaobank.codingtest.interfaces.v1.command.SignUpCommand;
import com.kakaobank.codingtest.interfaces.v1.viewmodel.SearchHistoryViewModel;
import com.kakaobank.codingtest.interfaces.v1.viewmodel.SignInViewModel;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/users")
public class UsersController {
    private final Interactor<SignUpRequestModel, SignUpResponseModel> signUpInteractor;
    private final Interactor<SignInRequestModel, SignInResponseModel> signInInteractor;
    private final Presenter<SignInResponseModel, SignInViewModel> signInPresenter;
    private final PlaceSearchReportHandler reportHandler;

    @ResponseStatus(CREATED)
    @PostMapping("sign-up")
    public void signUp(@RequestBody final SignUpCommand command) {
        signUpInteractor.interact(new SignUpRequestModel(command.getUsername(), command.getPassword()));
    }

    @PostMapping("sign-in")
    public SignInViewModel signIn(@RequestBody final SignInCommand command) {
        final var response = signInInteractor.interact(new SignInRequestModel(command.getUsername(), command.getPassword()));
        return signInPresenter.present(response);
    }

    @GetMapping("histories/search")
    public SearchHistoryViewModel searchHistory(final User user) {
        return new SearchHistoryViewModel(reportHandler.history(user));
    }
}
