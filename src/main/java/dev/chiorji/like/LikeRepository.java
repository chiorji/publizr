package dev.chiorji.like;

import java.util.*;
import org.springframework.jdbc.core.simple.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

@Transactional
@Repository
public class LikeRepository {
	private final JdbcClient jdbcClient;

	public LikeRepository(JdbcClient jdbcClient) {
		this.jdbcClient = jdbcClient;
	}

	public void likePost(LikeDTO likeDTO) {
		jdbcClient.sql("INSERT INTO LIKES (POST_ID, USER_ID) VALUES (?, ?)")
			.params(List.of(likeDTO.post_id(), likeDTO.user_id())).update();
	}

	public void unlikePost(LikeDTO likeDTO) {
		jdbcClient.sql("DELETE FROM LIKES WHERE POST_ID = ? AND USER_ID = ?")
			.params(List.of(likeDTO.post_id(), likeDTO.user_id()))
			.update();
	}

	public Integer getPostLikesCount(Integer postId) {
		return jdbcClient.sql("SELECT COUNT(*) FROM LIKES WHERE POST_ID = ?").params(postId).query(Integer.class).single();
	}

	public Optional<Like> getLikeByUserIdAndPostId(LikeDTO likeDTO) {
		return jdbcClient.sql("SELECT * FROM LIKES WHERE USER_ID = ? AND POST_ID = ?")
			.params(List.of(likeDTO.user_id(), likeDTO.post_id()))
			.query(Like.class).optional();
	}

	public Integer checkIfUserLikedPost(LikeDTO likeDTO) {
		return jdbcClient.sql("SELECT COUNT(*) FROM LIKES WHERE USER_ID = ? AND POST_ID = ?")
			.params(List.of(likeDTO.user_id(), likeDTO.post_id()))
			.query(Integer.class).single();
	}
}
