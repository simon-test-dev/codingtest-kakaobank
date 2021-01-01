package com.kakaobank.codingtest.interfaces;

import com.kakaobank.codingtest.interfaces.support.resolver.AuthenticatedUserMethodArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;


@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
public class InterfacesConfiguration implements WebMvcConfigurer {
    private final AuthenticatedUserMethodArgumentResolver authenticatedUserMethodArgumentResolver;

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authenticatedUserMethodArgumentResolver);
    }

}
