package com.kakaobank.codingtest.interfaces.support.resolver;

import com.kakaobank.codingtest.domain.model.user.User;
import com.kakaobank.codingtest.interfaces.support.security.SecureTokenHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import static org.apache.commons.lang.StringUtils.removeStart;
import static org.apache.commons.lang.StringUtils.trimToEmpty;

@RequiredArgsConstructor
@Component
public class AuthenticatedUserMethodArgumentResolver implements HandlerMethodArgumentResolver {
    private final SecureTokenHandler handler;

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return User.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(final MethodParameter parameter,
                                  final ModelAndViewContainer mavContainer,
                                  final NativeWebRequest webRequest,
                                  final WebDataBinderFactory binderFactory) {
        final var authentication = webRequest.getHeader("Authentication");
        final var token = removeStart(authentication, "Bearer");
        return handler.decode(trimToEmpty(token));
    }
}
