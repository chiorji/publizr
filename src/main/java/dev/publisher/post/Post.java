package dev.publisher.post;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

public record Post(
        @Id
        Integer id,

        @NotEmpty
        String title,

        @NotEmpty
        String content,

        String excerpt,

        String image_url,

        String tags,

        String status,

        Integer read_time,

        @NotEmpty
        @Nullable
        LocalDateTime created_at,

        @NotEmpty
        @Nullable
        LocalDateTime updated_at,

        @NotEmpty
        @NotNull
        Integer author_id,

        @NotEmpty
        @NotNull
        String category,

        @NotNull
        Boolean featured
) {
    public Post {
        if (created_at == null) {
            created_at = LocalDateTime.now();
        }
        updated_at = LocalDateTime.now();
    }
}
