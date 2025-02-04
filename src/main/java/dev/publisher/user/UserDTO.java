package dev.publisher.user;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

public record UserDTO(
        @Id
        Integer id,

        @NotEmpty
        @NotNull
        String username,

        @NotEmpty
        @NotNull
        String email,

        String role,

        String image_url,

        @NotNull
        LocalDateTime created_at,

        @NotNull
        LocalDateTime updated_at
) {
}
