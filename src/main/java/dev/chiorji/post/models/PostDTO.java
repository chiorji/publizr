package dev.chiorji.post.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jdk.jfr.BooleanFlag;
import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

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

	@NotNull
	@Schema(description = "A subtitle, or catchy paragraph from a post")
	String excerpt,

	@NotEmpty
	@Schema(description = "This is the post content")
	String content,

	@NotNull
	@PastOrPresent(message = "Post date cannot be in the future")
	@Schema(description = "The first time post was published")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	Date posted_on,

	@NotNull
	@PastOrPresent(message = "Last updated date cannot be in the future")
	@Schema(description = "The last time the post was edited")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	Date last_updated,

	@NotNull
	@Schema(description = "Categorizes post", example = "Technology, Finance")
	String category,

	@NotNull
	@Schema(description = "Post tags", example = "hacking, coding, java")
	String tags,

	@NotNull
	@Schema(description = "Poster card for post")
	String poster_card,

	@NotNull
	@Schema(description = "Post status, either published or draft", example = "Published")
	String status,

	@BooleanFlag
	@Schema(description = "Indicate whether a post is a featured top, featured post have larger display card")
	Boolean featured
) {

}
