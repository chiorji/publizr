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
		try {
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
					INNER JOIN CATEGORIES C P.CATEGORY = C.ID
					WHERE P.STATUS ILIKE '%PUBLISHED'
					ORDER BY P.POSTED_ON DESC
					"""
			).query(PostDTO.class).list();
		} catch (Exception e) {
			log.error("Error occurred while listing: {}", e.getMessage());
			throw new RuntimeException("Failed to retrieve posts", e);
		}
	}

	List<PostDTO> byAuthorId(Integer id) {
		try {
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
					P.LAST_UPDATED,
					P.READ_TIME,
					U.USERNAME
					FROM POSTS P
					INNER JOIN USERS U ON P.AUTHOR_ID = U.ID
					INNER JOIN IMAGES I ON P.POSTER_CARD = I.ID
					INNER JOIN CATEGORIES C P.CATEGORY = C.ID
					WHERE U.ID = :ID
					ORDER BY P.POSTED_ON DESC"""
			).param("ID", id).query(PostDTO.class).list();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error occurred while byAuthorId: {}", e.getMessage());
			throw new RuntimeException(e);
		}
	}

	public DeletePostDTO deletePostById(Integer id) {
		try {
			KeyHolder keyHolder = new GeneratedKeyHolder();
			var deletedId = jdbcClient.sql("DELETE FROM POSTS WHERE ID = :ID")
				.param("ID", id)
				.update(keyHolder);
			Assert.state(deletedId == 1, "Failed to delete post");
			int postId = (int) Objects.requireNonNull(keyHolder.getKeys()).get("ID");
			int imageId = (int) Objects.requireNonNull(keyHolder.getKeys().get("POSTER_CARD"));
			return new DeletePostDTO(postId, imageId);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public Integer update(PostDTO postDTO) {
		try {
			KeyHolder keyHolder = new GeneratedKeyHolder();
			var update = jdbcClient.sql(
					"""
						UPDATE POSTS SET TITLE = ?, EXCERPT = ?, CONTENT = ?, CATEGORY = ?, TAGS = ?, LAST_UPDATED = ?,
						STATUS = ? WHERE ID = ? AND AUTHOR_ID = ?
						""").params(List.of(
					postDTO.title(), postDTO.excerpt(), postDTO.content(), postDTO.category(),
					postDTO.tags(), LocalDateTime.now(), postDTO.status(), postDTO.id(), postDTO.author_id()
				))
				.update(keyHolder);
			return (Integer) Objects.requireNonNull(keyHolder.getKeys()).get("ID");
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
	}

	public List<PostDTO> recent() {
		try {
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
						INNER JOIN CATEGORIES C P.CATEGORY = C.ID
						WHERE	P.STATUS ILIKE '%PUBLISHED'	AND P.FEATURED = TRUE
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
						INNER JOIN CATEGORIES C P.CATEGORY = C.ID
						WHERE P.STATUS ILIKE '%PUBLISHED' AND P.FEATURED = FALSE
						ORDER BY P.POSTED_ON DESC LIMIT	9 )""").query(PostDTO.class).list();
		} catch (Exception e) {
			log.error("Error occurred while recent: {}", e.getMessage());
			throw new RuntimeException(e);
		}
	}

	public Integer totalEntries() {
		return jdbcClient.sql("SELECT COUNT(*) FROM POSTS").query(Integer.class).single();
	}

	public PostDTO findPostById(Integer id) {
		try {
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
					INNER JOIN CATEGORIES C P.CATEGORY = C.ID
					WHERE	P.ID = :ID
					""").param("ID", id).query(PostDTO.class).single();
		} catch (Exception e) {
			log.error("Error occurred while findPostById: {}", e.getMessage());
			throw new RuntimeException(e);
		}
	}
}