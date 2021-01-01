package com.kakaobank.codingtest.application.user;

import com.kakaobank.codingtest.application.shared.Interactor;
import com.kakaobank.codingtest.domain.model.user.UnauthorizedException;
import com.kakaobank.codingtest.domain.model.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SignInInteractor implements Interactor<SignInRequestModel, SignInResponseModel> {
    private final UserRepository repository;

    @Override
    public SignInResponseModel interact(final SignInRequestModel request) {
        final var user = repository.findOne(request.getUsername(), request.getPassword())
                                   .orElseThrow(UnauthorizedException::new);
        return new SignInResponseModel(user);
    }
}
