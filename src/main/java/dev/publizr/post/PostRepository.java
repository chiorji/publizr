package dev.publizr.post;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Objects;

@Repository
public class PostRepository {
	private final JdbcClient jdbcClient;

	public PostRepository(JdbcClient jdbcClient) {
		this.jdbcClient = jdbcClient;
	}

	void save(Post post) {
		var created = jdbcClient.sql(
				"""
						INSERT INTO POSTS (TITLE, EXCERPT, CONTENT, AUTHOR_ID, CATEGORY, POSTER_CARD, FEATURED, TAGS, STATUS)
						VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
					""")
			.params(List.of(
				post.title(), post.excerpt(), post.content(), post.author_id(), post.category(),
				post.poster_card(), post.featured(), post.tags(), post.status()
			))
			.update();
		Assert.state(created == 1, "failed to create post " + post.title());
	}

	List<PostDTO> findAll() {
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
				(P.ID) AS POST_ID,
				P.STATUS,
				P.TAGS,
				P.TITLE,
				P.LAST_UPDATED,
				A.USERNAME
				FROM POSTS P JOIN USERS A
				ON P.STATUS ILIKE '%PUBLISHED'
				ORDER BY P.POSTED_ON"""
		).query(PostDTO.class).list();
	}

	List<PostDTO> getPostsBelongingToAuthorWithId(Integer id) {
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
				(P.ID) AS POST_ID,
				P.STATUS,
				P.TAGS,
				P.TITLE,
				P.LAST_UPDATED,
				A.USERNAME
				FROM POSTS P JOIN USERS A
				ON P.AUTHOR_ID = :ID
				AND P.STATUS ILIKE '%PUBLISHED'
				ORDER BY P.POSTED_ON"""
		).param("ID", id).query(PostDTO.class).stream().toList();
	}

	public List<PostDTO> getRecentPosts() {
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
						P.ID AS POST_ID,
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
						P.POSTED_ON
					LIMIT
						1
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
						P.ID AS POST_ID,
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
						P.POSTED_ON
					LIMIT
						9
				)""").query(PostDTO.class).stream().toList();
	}

	public Integer deletePostWithProvidedId(Integer id) {
		try {
			KeyHolder keyHolder = new GeneratedKeyHolder();
			var deletedId = jdbcClient.sql("DELETE FROM POSTS WHERE POST_ID = :ID")
				.param("ID", id)
				.update(keyHolder);
			Assert.state(deletedId == 1, "Failed to delete post");
			return (Integer) Objects.requireNonNull(keyHolder.getKeys()).get("POST_ID");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public PostDTO updatePostWithProvidedPayload(PostDTO postDTO) {
		try {
			KeyHolder keyHolder = new GeneratedKeyHolder();
			var update = jdbcClient.sql(
					"""
						UPDATE POSTS SET TITLE = ?, EXCERPT = ?, CONTENT = ?, CATEGORY = ?, TAGS = ?
						WHERE POST_ID = ?
						""")
				.params(List.of(
					postDTO.title(),
					postDTO.excerpt(),
					postDTO.content(),
					postDTO.category(),
					postDTO.tags(),
					postDTO.post_id()
				))
				.update(keyHolder);
			Assert.state(update == 1, "Failed to update post " + postDTO.title());
			Integer postId = (Integer) Objects.requireNonNull(keyHolder.getKeys()).get("POST_ID");
			return this.findById(postId);
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
	}

	PostDTO findById(Integer id) {
		return jdbcClient.sql(
				"""
					SELECT
					P.CATEGORY,
					P.CONTENT,
					P.POSTED_ON,
					P.EXCERPT,
					P.FEATURED,
					P.POSTER_CARD,
					(P.ID) AS POST_ID,
					P.STATUS,
					P.TAGS,
					P.TITLE,
					P.LAST_UPDATED,
					P.AUTHOR_ID,
					A.USERNAME
					FROM POSTS P JOIN USERS A
					ON P.AUTHOR_ID = A.ID
					WHERE P.STATUS ILIKE '%PUBLISHED'
					AND P.ID = :ID
					""")
			.param("ID", id)
			.query(PostDTO.class)
			.single();
	}
}
