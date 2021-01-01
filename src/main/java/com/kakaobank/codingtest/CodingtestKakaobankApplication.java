package com.kakaobank.codingtest;

import com.kakaobank.codingtest.application.ApplicationConfiguration;
import com.kakaobank.codingtest.domain.DomainConfiguration;
import com.kakaobank.codingtest.infrastructure.InfrastructureConfiguration;
import com.kakaobank.codingtest.interfaces.InterfacesConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;

@SpringBootApplication(scanBasePackageClasses = {DomainConfiguration.class,
                                                 ApplicationConfiguration.class,
                                                 InfrastructureConfiguration.class,
                                                 InterfacesConfiguration.class})
@EnableCircuitBreaker
@EnableCaching
public class CodingtestKakaobankApplication {
    public static void main(final String[] args) {
        SpringApplication.run(CodingtestKakaobankApplication.class, args);
    }
}
