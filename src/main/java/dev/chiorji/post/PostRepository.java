package dev.chiorji.post;

import dev.chiorji.post.models.*;
import java.time.*;
import java.util.*;
import org.slf4j.*;
import org.springframework.jdbc.core.simple.*;
import org.springframework.jdbc.support.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;
import org.springframework.util.*;

@Transactional
@Repository
public class PostRepository {
	private static final Logger log = LoggerFactory.getLogger(PostRepository.class);
	private final JdbcClient jdbcClient;

	public PostRepository(JdbcClient jdbcClient) {
		this.jdbcClient = jdbcClient;
	}

	public void saveAll(List<Post> posts) {
		posts.forEach(this::save);
	}

	public Integer save(Post post) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		try {
			int readTime = readTimeCalculator(post.content().length(), 1);
			jdbcClient.sql(
					"""
							INSERT INTO POSTS (TITLE, EXCERPT, CONTENT, AUTHOR_ID, CATEGORY, POSTER_CARD, FEATURED, TAGS, STATUS, READ_TIME)
							VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
						""")
				.params(List.of(
					post.title(), post.excerpt(), post.content(), post.author_id(), post.category(),
					post.poster_card(), post.featured(), post.tags(), post.status(), readTime
				)).update(keyHolder);
			return (Integer) Objects.requireNonNull(keyHolder.getKeys()).get("ID");
		} catch (Exception e) {
			log.error("Error occurred while saving: {}", e.getMessage());
			throw new RuntimeException(e);
		}
	}

	public int readTimeCalculator(int wordCount, int imageCount) {
		int wordsPerMinute = 240;
		int secondsPerImage = 12;
		int readingTimeMinutes = wordCount / wordsPerMinute;
		int imageTimeMinutes = (imageCount * secondsPerImage) / 60;
		return readingTimeMinutes + imageTimeMinutes;
	}

	public List<PostDTO> list() {
		return jdbcClient.sql(
			"""
				SELECT
				P.AUTHOR_ID,
				C.NAME AS CATEGORY,
				P.CONTENT,
				P.POSTED_ON,
				P.EXCERPT,
				P.FEATURED,
				I.URL,
				P.ID,
				P.STATUS,
				P.TAGS,
				P.TITLE,
				P.READ_TIME,
				P.LAST_UPDATED,
				U.USERNAME FROM POSTS P
				INNER JOIN USERS U ON P.AUTHOR_ID = U.ID
				INNER JOIN IMAGES I ON P.POSTER_CARD = I.ID
				INNER JOIN CATEGORIES C ON P.CATEGORY = C.ID
				WHERE P.STATUS ILIKE '%PUBLISHED' AND P.IS_DELETED = FALSE
				ORDER BY P.POSTED_ON DESC
				"""
		).query(PostDTO.class).list();
	}

	List<PostDTO> byAuthorId(Integer id) {
		return jdbcClient.sql(
			"""
				SELECT
				P.AUTHOR_ID,
				C.NAME AS CATEGORY,
				P.CONTENT,
				P.POSTED_ON,
				P.EXCERPT,
				P.FEATURED,
				P.IS_DELETED,
				I.URL,
				P.ID,
				P.STATUS,
				P.TAGS,
				P.TITLE,
				P.LAST_UPDATED,
				P.READ_TIME,
				U.USERNAME
				FROM POSTS P
				INNER JOIN USERS U ON P.AUTHOR_ID = U.ID
				INNER JOIN IMAGES I ON P.POSTER_CARD = I.ID
				INNER JOIN CATEGORIES C ON P.CATEGORY = C.ID
				WHERE U.ID = :ID
				ORDER BY P.POSTED_ON DESC"""
		).param("ID", id).query(PostDTO.class).list();
	}

	public KeyHolder deletePostById(Integer id) {
		try {
			KeyHolder keyHolder = new GeneratedKeyHolder();
			var deletedId = jdbcClient.sql("DELETE FROM POSTS WHERE ID = :ID RETURNING *")
				.param("ID", id)
				.update(keyHolder);
			Assert.state(deletedId == 1, "Failed to delete post");
			return keyHolder;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public KeyHolder updatePost(PostUpdateDTO postUpdateDTO) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcClient.sql(
			"""
				UPDATE POSTS SET TITLE = ?,	EXCERPT = ?, CONTENT = ?,	CATEGORY = ?,	TAGS = ?, LAST_UPDATED = ?, STATUS = ?
				 WHERE ID = ? AND AUTHOR_ID = ?
				""").params(List.of(
			postUpdateDTO.title(), postUpdateDTO.excerpt(), postUpdateDTO.content(), postUpdateDTO.category(),
			postUpdateDTO.tags(), LocalDateTime.now(), postUpdateDTO.status(),
			postUpdateDTO.id(), postUpdateDTO.author_id()
		)).update(keyHolder);
		return keyHolder;
	}

	public List<PostDTO> getTenMostRecentPosts() {
		return jdbcClient.sql(
			"""
				(
					SELECT
					P.AUTHOR_ID,
					P.FEATURED,
					C.NAME AS CATEGORY,
					P.CONTENT,
					P.POSTED_ON,
					P.EXCERPT,
					I.URL,
					P.ID,
					P.STATUS,
					P.TAGS,
					P.TITLE,
					P.LAST_UPDATED,
					P.READ_TIME,
					U.USERNAME FROM	POSTS P
					INNER JOIN USERS U ON P.AUTHOR_ID = U.ID
					INNER JOIN IMAGES I ON P.POSTER_CARD = I.ID
					INNER JOIN CATEGORIES C ON P.CATEGORY = C.ID
					WHERE	P.STATUS ILIKE '%PUBLISHED'	AND P.FEATURED = TRUE AND P.IS_DELETED = FALSE
					ORDER BY	P.POSTED_ON DESC LIMIT	1
				)
				UNION ALL
				(
					SELECT
					P.AUTHOR_ID,
					P.FEATURED,
					C.NAME AS CATEGORY,
					P.CONTENT,
					P.POSTED_ON,
					P.EXCERPT,
					I.URL,
					P.ID,
					P.STATUS,
					P.TAGS,
					P.TITLE,
					P.LAST_UPDATED,
					P.READ_TIME,
					U.USERNAME	FROM	POSTS P
					INNER JOIN USERS U ON P.AUTHOR_ID = U.ID
					INNER JOIN IMAGES I ON P.POSTER_CARD = I.ID
					INNER JOIN CATEGORIES C ON P.CATEGORY = C.ID
					WHERE P.STATUS ILIKE '%PUBLISHED' AND P.FEATURED = FALSE AND P.IS_DELETED = FALSE
					ORDER BY P.POSTED_ON DESC LIMIT	9 )""").query(PostDTO.class).list();
	}

	public Integer totalEntries() {
		return jdbcClient.sql("SELECT COUNT(*) FROM POSTS").query(Integer.class).single();
	}

	public PostDTO findPostById(Integer id) {
		return jdbcClient.sql(
			"""
				SELECT
				P.AUTHOR_ID,
				C.NAME AS CATEGORY,
				P.CONTENT,
				P.POSTED_ON,
				P.EXCERPT,
				P.FEATURED,
				I.URL,
				P.ID,
				P.STATUS,
				P.TAGS,
				P.TITLE,
				P.READ_TIME,
				P.LAST_UPDATED,
				U.USERNAME
				FROM	POSTS P
				INNER JOIN USERS U ON P.AUTHOR_ID = U.ID
				INNER JOIN IMAGES I ON P.POSTER_CARD = I.ID
				INNER JOIN CATEGORIES C ON P.CATEGORY = C.ID
				WHERE	P.ID = :ID
				""").param("ID", id).query(PostDTO.class).single();
	}

	public KeyHolder softDeletePostByIdAndAuthorId(PostDeleteDTO postDeleteDTO) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcClient.sql("UPDATE POSTS SET IS_DELETED = TRUE WHERE ID = ? AND AUTHOR_ID = ?")
			.params(List.of(postDeleteDTO.id(), postDeleteDTO.author_id()))
			.update(keyHolder);
		return keyHolder;
	}

	public Boolean updatePostFeatureStatus(Integer id) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcClient.sql("UPDATE POSTS SET FEATURED = TRUE WHERE ID = :ID RETURNING *")
			.param("ID", id).update(keyHolder);
		Integer postId = (Integer) Objects.requireNonNull(keyHolder.getKeys()).get("ID");
		return postId >= 1;
	}
}