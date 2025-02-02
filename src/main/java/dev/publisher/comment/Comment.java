package dev.publisher.comment;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

public record Comment(
        @Id
        Integer id,

        @NotEmpty
        @NotNull
        String content,

        @NotEmpty
        @NotNull
        LocalDateTime created_at,

        @NotNull
        Integer author_id,

        @NotNull
        Integer post_id
) {
}
