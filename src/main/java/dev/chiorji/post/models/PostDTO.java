package dev.chiorji.post.models;

import io.swagger.v3.oas.annotations.media.*;
import jakarta.validation.constraints.*;
import java.util.*;
import jdk.jfr.*;
import org.springframework.data.annotation.*;
import org.springframework.format.annotation.*;

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
	String url,

	@NotNull
	@Schema(description = "Post status, either published or draft", example = "Published")
	String status,

	@BooleanFlag
	@Schema(description = "Indicate whether a post is a featured top, featured post have larger display card")
	Boolean featured
) {

}
