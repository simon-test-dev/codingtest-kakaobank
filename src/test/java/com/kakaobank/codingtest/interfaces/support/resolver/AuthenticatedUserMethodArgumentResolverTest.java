package com.kakaobank.codingtest.interfaces.support.resolver;

import com.kakaobank.codingtest.domain.model.user.User;
import com.kakaobank.codingtest.interfaces.support.security.SecureTokenHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class AuthenticatedUserMethodArgumentResolverTest {
    private SecureTokenHandler handler;
    private AuthenticatedUserMethodArgumentResolver resolver;
    private NativeWebRequest request;

    @BeforeEach
    void setUp() {
        handler = mock(SecureTokenHandler.class);
        resolver = new AuthenticatedUserMethodArgumentResolver(handler);
        request = mock(NativeWebRequest.class);
    }

    @Test
    void testSupportsParameter() {
        final var parameter = mock(MethodParameter.class);
        doReturn(User.class).when(parameter).getParameterType();
        assertThat(resolver.supportsParameter(parameter)).isTrue();
    }

    @Test
    void testNotExistsHeader() {
        doReturn(null).when(request).getHeader("Authentication");
        resolver.resolveArgument(null, null, request, null);
        verify(handler).decode("");
    }

    @Test
    void testEmptyHeader() {
        doReturn("").when(request).getHeader("Authentication");
        resolver.resolveArgument(null, null, request, null);
        verify(handler).decode("");
    }

    @Test
    void testRemoveBearer() {
        doReturn("Bearer").when(request).getHeader("Authentication");
        resolver.resolveArgument(null, null, request, null);
        verify(handler).decode("");
    }

    @Test
    void testResolveArgument() {
        doReturn("Bearer 123").when(request).getHeader("Authentication");
        resolver.resolveArgument(null, null, request, null);
        verify(handler).decode("123");
    }
}