package com.kakaobank.codingtest.domain.model.user;

import com.kakaobank.codingtest.domain.shared.Entity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.util.ClassUtils;

import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor
@ToString
@Getter
public class User implements Entity {
    private final UUID id;
    private final String username;

    @Override
    public boolean equals(final Object that) {
        if (this == that) {
            return true;
        }
        if (that == null || !ClassUtils.isAssignableValue(User.class, that)) {
            return false;
        }
        final User user = (User) that;
        return Objects.equals(getId(), user.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
