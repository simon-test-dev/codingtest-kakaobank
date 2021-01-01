package com.kakaobank.codingtest.interfaces.support.security;

import com.kakaobank.codingtest.domain.model.user.UnauthorizedException;
import com.kakaobank.codingtest.domain.model.user.User;
import com.kakaobank.codingtest.domain.model.user.UserNotFoundException;
import com.kakaobank.codingtest.domain.model.user.UserRepository;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.lang.StringUtils.isBlank;

@Service
public class SecureTokenHandlerImpl implements SecureTokenHandler {
    private static final String USER_ID = "userId";
    private static final long EXPIRE_DURATION = Duration.ofHours(3).toMillis();
    private final UserRepository repository;
    private final SecretKey key;
    private final JwtParser parser;

    public SecureTokenHandlerImpl(final UserRepository repository,
                                  @Value("${security.jwt.key}") final String key) {
        this.repository = repository;
        this.key = Keys.hmacShaKeyFor(key.getBytes(UTF_8));
        this.parser = Jwts.parserBuilder()
                          .setSigningKey(this.key)
                          .build();
    }

    @Override
    public String encode(final User user) {
        return Jwts.builder()
                   .setClaims(Map.of(USER_ID, user.getId().toString()))
                   .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_DURATION))
                   .signWith(key)
                   .compact();
    }

    @Override
    public User decode(final String token) {
        if (isBlank(token)) {
            throw new UnauthorizedException("token is blank");
        }
        try {
            final var claimsJws = parser.parseClaimsJws(token);
            final var body = claimsJws.getBody();
            final var userId = body.get(USER_ID, String.class);
            return repository.findOne(UUID.fromString(userId))
                             .orElseThrow(UserNotFoundException::new);
        } catch (final Exception cause) {
            throw new UnauthorizedException(cause);
        }
    }
}
