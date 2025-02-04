package dev.publisher.user;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

public record User(

        @NotEmpty
        @NotNull
        String username,

        @NotEmpty
        @NotNull
        String email,

        @NotEmpty
        @NotNull
        String password

) {
}
