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

        String poster_card,

        String tags,

        String status,

        Integer read_time,

        @NotEmpty
        @Nullable
        LocalDateTime posted_on,

        @NotEmpty
        @Nullable
        LocalDateTime last_updated,

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
        if (posted_on == null) {
            posted_on = LocalDateTime.now();
        }
        last_updated = LocalDateTime.now();
    }
}
