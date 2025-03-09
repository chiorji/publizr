package dev.chiorji.like;

import dev.chiorji.execption.*;
import jakarta.validation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/likes")
public class LikeController {

	private final LikeService likeService;

	public LikeController(LikeService likeService) {
		this.likeService = likeService;
	}

	@GetMapping("/check")
	public ResponseEntity<Boolean> checkIfUserLikedPost(@ModelAttribute LikeDTO likeDTO) {
		try {
			Boolean isLiked = likeService.checkIfUserLikedPost(likeDTO);
			return new ResponseEntity<>(isLiked, HttpStatus.OK);
		} catch (Exception e) {
			throw new Post404Exception(e.getMessage());
		}
	}

	@GetMapping("/{postId}")
	public ResponseEntity<Integer> getPostLikesCount(@PathVariable @Valid Integer postId) {
		try {
		Integer count = likeService.getPostLikesCount(postId);
		return new ResponseEntity<>(count, HttpStatus.OK);
		} catch (Exception e) {
			throw new Post404Exception(e.getMessage());
		}
	}

	@PostMapping("/like")
	public ResponseEntity<Boolean> likePost(@RequestBody @Valid LikeDTO likeDTO) {
		try {
		Boolean likedPost = likeService.likePost(likeDTO);
		return new ResponseEntity<>(likedPost, HttpStatus.OK);
		} catch (Exception e) {
			throw new Post404Exception(e.getMessage());
		}
	}
}
