package dev.publizr.post;

import dev.publizr.post.models.Post;
import dev.publizr.post.models.PostDTO;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Transactional
@Repository
public class PostRepository {
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

			var created = jdbcClient.sql(
					"""
							INSERT INTO POSTS (TITLE, EXCERPT, CONTENT, AUTHOR_ID, CATEGORY, POSTER_CARD, FEATURED, TAGS, STATUS)
							VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
						""")
				.params(List.of(
					post.title(),
					post.excerpt(),
					post.content(),
					post.author_id(),
					post.category(),
					post.poster_card(),
					post.featured(),
					post.tags(),
					post.status()
				))
				.update(keyHolder);
			return (Integer) Objects.requireNonNull(keyHolder.getKeys()).get("ID");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<PostDTO> list() {
		try {
			return jdbcClient.sql(
				"""
					SELECT
						P.AUTHOR_ID,
						P.CATEGORY,
						P.CONTENT,
						P.POSTED_ON,
						P.EXCERPT,
						P.FEATURED,
						P.POSTER_CARD,
						P.ID,
						P.STATUS,
						P.TAGS,
						P.TITLE,
						P.LAST_UPDATED,
						U.USERNAME
					FROM
						POSTS P
						JOIN USERS U ON P.AUTHOR_ID = U.ID AND P.STATUS ILIKE '%PUBLISHED'
					ORDER BY
						P.POSTED_ON DESC
					"""
			).query(PostDTO.class).list();
		} catch (Exception e) {
			throw new RuntimeException("Failed to retrieve posts", e);
		}
	}

	List<PostDTO> byAuthorId(Integer id) {
		try {
			return jdbcClient.sql(
				"""
					SELECT
					P.AUTHOR_ID,
					P.CATEGORY,
					P.CONTENT,
					P.POSTED_ON,
					P.EXCERPT,
					P.FEATURED,
					P.POSTER_CARD,
					P.ID,
					P.STATUS,
					P.TAGS,
					P.TITLE,
					P.LAST_UPDATED,
					U.USERNAME
					FROM POSTS P JOIN USERS U ON P.AUTHOR_ID = U.ID
					WHERE U.ID = :ID
					ORDER BY P.POSTED_ON DESC"""
			).param("ID", id).query(PostDTO.class).list();
		} catch (Exception e) {
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
							P.CATEGORY,
							P.CONTENT,
							P.POSTED_ON,
							P.EXCERPT,
							P.POSTER_CARD,
							P.ID,
							P.STATUS,
							P.TAGS,
							P.TITLE,
							P.LAST_UPDATED,
							U.USERNAME
						FROM
							POSTS P
							INNER JOIN USERS U ON P.AUTHOR_ID = U.ID
						WHERE
							P.STATUS ILIKE '%PUBLISHED'
							AND P.FEATURED = TRUE
						ORDER BY
							P.POSTED_ON DESC
						LIMIT	1
					)
					UNION ALL
					(
						SELECT
						  P.AUTHOR_ID,
							P.FEATURED,
							P.CATEGORY,
							P.CONTENT,
							P.POSTED_ON,
							P.EXCERPT,
							P.POSTER_CARD,
							P.ID,
							P.STATUS,
							P.TAGS,
							P.TITLE,
							P.LAST_UPDATED,
							U.USERNAME
						FROM
							POSTS P
							INNER JOIN USERS U ON P.AUTHOR_ID = U.ID
						WHERE
							P.STATUS ILIKE '%PUBLISHED'
							AND P.FEATURED = FALSE
						ORDER BY
							P.POSTED_ON DESC
						LIMIT	9
					)""").query(PostDTO.class).list();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public Integer deleteById(Integer id) {
		try {
			KeyHolder keyHolder = new GeneratedKeyHolder();
			var deletedId = jdbcClient.sql("DELETE FROM POSTS WHERE ID = :ID")
				.param("ID", id)
				.update(keyHolder);
			Assert.state(deletedId == 1, "Failed to delete post");
			return (Integer) Objects.requireNonNull(keyHolder.getKeys()).get("ID");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public Integer update(PostDTO postDTO) {
		try {
			KeyHolder keyHolder = new GeneratedKeyHolder();
			var update = jdbcClient.sql(
					"""
						UPDATE POSTS SET TITLE = ?, EXCERPT = ?, CONTENT = ?, CATEGORY = ?, TAGS = ?, LAST_UPDATED = ?
						WHERE ID = ? AND AUTHOR_ID = ?
						""")
				.params(List.of(
					postDTO.title(),
					postDTO.excerpt(),
					postDTO.content(),
					postDTO.category(),
					postDTO.tags(),
					LocalDateTime.now(),
					postDTO.id(),
					postDTO.author_id()
				))
				.update(keyHolder);
			return (Integer) Objects.requireNonNull(keyHolder.getKeys()).get("ID");
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
	}

	PostDTO findById(Integer id) {
		try {
			return jdbcClient.sql(
					"""
						SELECT
						P.CATEGORY,
						P.CONTENT,
						P.POSTED_ON,
						P.EXCERPT,
						P.FEATURED,
						P.POSTER_CARD,
						P.ID,
						P.STATUS,
						P.TAGS,
						P.TITLE,
						P.LAST_UPDATED,
						P.AUTHOR_ID,
						U.USERNAME
						FROM POSTS P JOIN USERS U	ON P.AUTHOR_ID = U.ID AND P.ID = :ID
						WHERE P.STATUS ILIKE '%PUBLISHED'
						""")
				.param("ID", id)
				.query(PostDTO.class)
				.single();
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
	}

	public PostDTO findPostById(Integer id) {
		try {
			return jdbcClient.sql(
				"""
					SELECT
						P.AUTHOR_ID,
						P.CATEGORY,
						P.CONTENT,
						P.POSTED_ON,
						P.EXCERPT,
						P.FEATURED,
						P.POSTER_CARD,
						P.ID,
						P.STATUS,
						P.TAGS,
						P.TITLE,
						P.LAST_UPDATED,
						U.USERNAME
					FROM
						POSTS P
						JOIN USERS U ON P.AUTHOR_ID = U.ID
					WHERE
						P.ID = :ID
					"""
			).param("ID", id).query(PostDTO.class).single();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public Integer totalEntries() {
		return jdbcClient.sql("SELECT COUNT(*) FROM POSTS").query(Integer.class).single();
	}
}
