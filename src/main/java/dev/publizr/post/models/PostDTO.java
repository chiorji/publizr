package dev.publizr.post.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jdk.jfr.BooleanFlag;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Schema(description = "Post Data Transfer Object")
public record PostDTO(
	@NotEmpty
	@Schema(description = "This is the author's display name")
	String username,

	@Id
	@NotNull
	@Schema(description = "Author's identifier")
	Integer author_id,

	@Id
	@NotNull
	@Schema(description = "An identifier for the post")
	Integer id,

	@NotEmpty
	@Schema(description = "This is the post's title")
	String title,

	@Nullable
	@Schema(description = "A subtitle, or catchy paragraph from a post")
	String excerpt,

	@NotEmpty
	@Schema(description = "This is the post content")
	String content,

	@NotNull
	@PastOrPresent
	@Schema(description = "The first time post was published")
	LocalDateTime posted_on,

	@NotNull
	@PastOrPresent
	@Schema(description = "The last time the post was edited")
	LocalDateTime last_updated,

	@NotEmpty
	@Schema(description = "Categorizes post", example = "Technology, Finance")
	String category,

	@Nullable
	@Schema(description = "Post tags", example = "hacking, coding, java")
	String tags,

	@NotNull
	@Schema(description = "Poster card for post")
	String poster_card,

	@NotEmpty
	@Schema(description = "Post status", example = "Published or Draft")
	String status,

	@BooleanFlag
	@Schema(description = "Indicate whether a post is a featured top, featured post have larger display card")
	Boolean featured
) {
}
