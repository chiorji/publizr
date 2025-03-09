package dev.chiorji.like;

import dev.chiorji.post.*;
import dev.chiorji.post.models.*;
import dev.chiorji.user.*;
import dev.chiorji.user.models.*;
import java.util.*;
import org.springframework.stereotype.*;

@Service
public class LikeService {
	private final LikeRepository likeRepository;
	private final UserRepository userRepository;
	private final PostRepository postRepository;

	public LikeService(LikeRepository likeRepository, UserRepository userRepository, PostRepository postRepository) {
		this.likeRepository = likeRepository;
		this.userRepository = userRepository;
		this.postRepository = postRepository;
	}

	public boolean likePost(LikeDTO likeDTO) {
			Optional<Like> like = likeRepository.getLikeByUserIdAndPostId(likeDTO);
			if (like.isPresent()) {
				unlikePost(likeDTO);
				return false;
			}

		// check user and post exists
			UserDTO userDTO = userRepository.findByUserId(likeDTO.user_id());
			PostDTO postDTO = postRepository.findPostById(likeDTO.post_id());
		if (userDTO == null || postDTO == null) throw new RuntimeException("Failed: Post may have been deleted.");
			likeRepository.likePost(likeDTO);
			return true;
	}

	public void unlikePost(LikeDTO likeDTO) {
		likeRepository.unlikePost(likeDTO);
	}

	public Integer getPostLikesCount(Integer postId) {
		return likeRepository.getPostLikesCount(postId);
	}

	public boolean checkIfUserLikedPost(LikeDTO likeDTO) {
		Integer likeCount = likeRepository.checkIfUserLikedPost(likeDTO);
		return likeCount > 0;
	}
}
