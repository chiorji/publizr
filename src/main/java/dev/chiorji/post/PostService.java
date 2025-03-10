package dev.chiorji.post;

import dev.chiorji.execption.*;
import dev.chiorji.image.*;
import dev.chiorji.like.*;
import dev.chiorji.post.models.*;
import dev.chiorji.util.*;
import java.util.*;
import org.slf4j.*;
import org.springframework.jdbc.support.*;
import org.springframework.stereotype.*;

@Service
public class PostService {
	private static final Logger log = LoggerFactory.getLogger(PostService.class);
	private final PostRepository postRepository;
	private final ImageService imageService;
	private final LikeService likeService;
	private final Constant constant;

	public PostService(PostRepository postRepository, ImageService imageService, LikeService likeService, Constant constant) {
		this.postRepository = postRepository;
		this.imageService = imageService;
		this.likeService = likeService;
		this.constant = constant;
	}

	public List<PostDTO> list() {
		try {
			return postRepository.list();
		} catch (Exception e) {
			log.error("Post list failed: -- '{}' -- ", e.getMessage());
			throw new RuntimeException("Failed to retrieve posts", e);
		}
	}

	public PostDTO publishPost(PublishDTO publishDTO) {
		try {
//			if (!constant.sameAuthorIdAndClaimId(roleInfo.id(), publishDTO.author_id())) {
//				throw new AuthenticationFailedException("Failed! Mismatching identities");
//			}
			Image image = imageService.uploadImage(new ImageUploadDTO(publishDTO.title(), publishDTO.poster_card()));
			Post post = new Post(
				publishDTO.title(), publishDTO.content(), publishDTO.excerpt(), image.id(), publishDTO.tags(), publishDTO.status(), publishDTO.author_id(),
				publishDTO.category(), false
			);
			Integer postId = postRepository.save(post);
			return postRepository.findPostById(postId);
		} catch (Exception e) {
			log.error("Post publish failed -- '{}' -- ", e.getMessage());
			throw new PublishFailedException(e.getMessage());
		}
	}

	public List<PostDTO> getPostByAuthorId(Integer authorId) {
		try {
			return postRepository.byAuthorId(authorId);
		} catch (Exception e) {
			log.error("Get by author ID Failed -- '{}' -- ", e.getMessage());
			throw new RuntimeException(e);
		}
	}

	public List<PostDTO> getTenMostRecentPosts() {
		Integer totalPosts = getPostTotalEntries();
		if (totalPosts < 6) return postRepository.list();
		return postRepository.getTenMostRecentPosts();
	}

	public Integer getPostTotalEntries() {
		return postRepository.totalEntries();
	}

	public void deletePostById(Integer postId) {
		try {
			Integer postLikes = likeService.getPostLikesCount(postId);
			if (postLikes > 0) {
				PostDTO postDTO = postRepository.findPostById(postId);
				LikeDTO likeDTO = new LikeDTO(postDTO.id(), postDTO.author_id());
				likeService.unlikePost(likeDTO);
			}

			KeyHolder keyHolder = postRepository.deletePostById(postId);
			if (keyHolder.getKeyList().isEmpty()) throw new RuntimeException("Failed! Post was not deleted due to error.");
			Integer deletedPostImageId = (Integer) Objects.requireNonNull(keyHolder.getKeys()).get("POSTER_CARD");
			imageService.deleteImageById(deletedPostImageId);
		} catch (Exception e) {
			log.error("Delete failed -- '{}' -- ", e.getMessage());
			throw new RuntimeException(e);
		}
	}

	public PostDTO updatePost(PostUpdateDTO postUpdateDTO) {
		KeyHolder keyHolder = postRepository.updatePost(postUpdateDTO);
		Integer postId = (Integer) Objects.requireNonNull(keyHolder.getKeys()).get("ID");
		return postRepository.findPostById(postId);
	}

	public void softDeletePostByIdAndAuthorId(PostDeleteDTO postDeleteDTO) {
		try {
			Integer postLikes = likeService.getPostLikesCount(postDeleteDTO.id());
			if (postLikes > 0) {
				PostDTO postDTO = postRepository.findPostById(postDeleteDTO.id());
				LikeDTO likeDTO = new LikeDTO(postDTO.id(), postDTO.author_id());
				likeService.unlikePost(likeDTO);
			}

			KeyHolder keyHolder = postRepository.softDeletePostByIdAndAuthorId(postDeleteDTO);
			if (keyHolder.getKeyList().isEmpty()) throw new RuntimeException("Failed! Post was not deleted due to error.");
		} catch (Exception e) {
			log.error("Soft Delete Failed -- '{}' -- ", e.getMessage());
			throw new RuntimeException(e);
		}
	}

	public Boolean updatePostFeatureStatus(Integer id) {
		return postRepository.updatePostFeatureStatus(id);
	}
}
