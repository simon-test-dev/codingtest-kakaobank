package com.kakaobank.codingtest.application.user;

import com.kakaobank.codingtest.application.shared.Interactor;
import com.kakaobank.codingtest.domain.model.user.UserFactory;
import com.kakaobank.codingtest.domain.model.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class SignUpInteractor implements Interactor<SignUpRequestModel, SignUpResponseModel> {
    private final UserFactory factory;
    private final UserRepository repository;

    @Transactional
    @Override
    public SignUpResponseModel interact(final SignUpRequestModel request) {
        final var user = factory.create(request.getUsername());
        repository.store(user, request.getPassword());
        return new SignUpResponseModel(user);
    }
}
