package dev.publizr.post;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/posts")
public class PostController {

	private final PostRepository postRepository;

	public PostController(PostRepository postRepository) {
		this.postRepository = postRepository;
	}

	@GetMapping("")
	List<PostDTO> findAll() {
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
	Optional<List<PostDTO>> findAllByAuthorId(@PathVariable Integer id) {
		return Optional.ofNullable(postRepository.findAllByAuthorId(id));
	}

	@GetMapping("/recent")
	Optional<List<PostDTO>> findOverview() {
		return Optional.ofNullable(postRepository.findOverview());
	}
}
