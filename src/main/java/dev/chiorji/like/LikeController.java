package dev.chiorji.like;

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
		Boolean isLiked = likeService.checkIfUserLikedPost(likeDTO);
		return new ResponseEntity<>(isLiked, HttpStatus.OK);
	}

	@GetMapping("/{postId}")
	public ResponseEntity<Integer> getPostLikesCount(@PathVariable @Valid Integer postId) {
		Integer count = likeService.getPostLikesCount(postId);
		return new ResponseEntity<>(count, HttpStatus.OK);
	}

	@PostMapping("/like")
	public ResponseEntity<Boolean> likePost(@RequestBody @Valid LikeDTO likeDTO) {
		Boolean likedPost = likeService.likePost(likeDTO);
		return new ResponseEntity<>(likedPost, HttpStatus.OK);
	}
}
