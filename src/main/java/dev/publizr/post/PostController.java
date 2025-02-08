package dev.publizr.post;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/posts")
public class PostController {

	private final PostRepository postRepository;

	public PostController(PostRepository postRepository) {
		this.postRepository = postRepository;
	}

	@GetMapping("")
	List<PostDTO> list() {
		return postRepository.findAll();
	}

	@GetMapping("/{id}")
	Optional<PostDTO> findById(@PathVariable Integer id) {
		return postRepository.findById(id);
	}

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/new")
	void create(@RequestBody Post post) {
		postRepository.save(post);
	}

	@GetMapping("/author/{id}")
	Optional<List<PostDTO>> getPostsBelongingToAuthorWithId(@PathVariable Integer id) {
		return Optional.ofNullable(postRepository.getPostsBelongingToAuthorWithId(id));
	}

	@GetMapping("/recent")
	Optional<List<PostDTO>> recent() {
		return Optional.ofNullable(postRepository.getRecentPosts());
	}

	@DeleteMapping("/{id}")
	ResponseEntity<Map<String, String>> deletePostWithProvidedId(@PathVariable Integer id) {
		try {
			Integer deletedPostId = postRepository.deletePostWithProvidedId(id);
			if (deletedPostId < 0) throw new RuntimeException("Failed! Post was not deleted due to error.");
			Map<String, String> map = new HashMap<>();
			map.put("success", String.valueOf(true));
			map.put("message", "Post deleted successfully");
			return new ResponseEntity<>(map, HttpStatus.OK);
		} catch (RuntimeException e) {
			Map<String, String> map = new HashMap<>();
			map.put("success", String.valueOf(false));
			map.put("message", e.getLocalizedMessage());
			return new ResponseEntity<>(map, HttpStatus.OK);
		}
	}
}
