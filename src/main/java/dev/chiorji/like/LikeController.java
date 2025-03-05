package dev.chiorji.like;

import jakarta.validation.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/likes")
public class LikeController {

	private final LikeService likeService;

	public LikeController(LikeService likeService) {
		this.likeService = likeService;
	}

	@GetMapping("/check")
	public boolean checkIfUserLikedPost(@ModelAttribute LikeDTO likeDTO) {
		return likeService.checkIfUserLikedPost(likeDTO);
	}

	@GetMapping("/{postId}")
	public Integer getPostLikesCount(@PathVariable @Valid Integer postId) {
		return likeService.getPostLikesCount(postId);
	}

	@PostMapping("/like")
	public boolean likePost(@RequestBody @Valid LikeDTO likeDTO) {
		return likeService.likePost(likeDTO);
	}
}
