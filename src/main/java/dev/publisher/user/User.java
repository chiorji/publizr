package dev.publisher.user;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

public record User(
        @Id
        Integer id,

        @NotEmpty
        @NotNull
        String username,

        @NotEmpty
        @NotNull
        String email,

        @NotEmpty
        @NotNull
        String password,

        @NotNull
        Integer role,

        @NotNull
        LocalDateTime created_at,

        @NotNull
        LocalDateTime updated_at
) {
    public User {
        if (created_at == null) {
            created_at = LocalDateTime.now();
        }
        updated_at = LocalDateTime.now();
    }
}
