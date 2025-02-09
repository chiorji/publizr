package dev.publizr.post;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/posts")
public class PostController {

	private final PostRepository postRepository;

	public PostController(PostRepository postRepository) {
		this.postRepository = postRepository;
	}

	@GetMapping("")
	ResponseEntity<Map<String, Object>> list() {
		Map<String, Object> response = new HashMap<>();
		try {
			List<PostDTO> postDTO = postRepository.list();
			response.put("success", true);
			response.put("data", postDTO);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (RuntimeException e) {
			response.put("success", false);
			response.put("message", e.getLocalizedMessage());
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
	}

	@GetMapping("/{id}")
	ResponseEntity<Map<String, Object>> findById(@PathVariable Integer id) {
		Map<String, Object> response = new HashMap<>();
		try {
			PostDTO postDTO = postRepository.findById(id);
			response.put("success", true);
			response.put("data", postDTO);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (RuntimeException e) {

			response.put("success", false);
			response.put("message", e.getLocalizedMessage());
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
	}

	@PostMapping("/publish")
	ResponseEntity<Map<String, Object>> create(@RequestBody Post post) {
		Map<String, Object> response = new HashMap<>();

		try {
			Integer postId = postRepository.save(post);
			PostDTO newPost = postRepository.findById(postId);

			response.put("success", true);
			response.put("data", newPost);
			return new ResponseEntity<>(response, HttpStatus.CREATED);
		} catch (RuntimeException e) {
			response.put("success", false);
			response.put("message", e.getLocalizedMessage());
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
	}

	@GetMapping("/author/{id}")
	ResponseEntity<Map<String, Object>> getByAuthorId(@PathVariable Integer id) {
		Map<String, Object> response = new HashMap<>();
		try {
			List<PostDTO> postDTO = postRepository.getByAuthorId(id);
			response.put("success", true);
			response.put("data", postDTO);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (RuntimeException e) {
			response.put("success", false);
			response.put("message", e.getLocalizedMessage());
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
	}

	@GetMapping("/recent")
	ResponseEntity<Map<String, Object>> recent() {
		Map<String, Object> response = new HashMap<>();
		try {
			List<PostDTO> postDTO = postRepository.recent();
			response.put("success", true);
			response.put("data", postDTO);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (RuntimeException e) {
			response.put("success", false);
			response.put("message", e.getLocalizedMessage());
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
	}

	@DeleteMapping("/{id}")
	ResponseEntity<Map<String, Object>> deletePostWithProvidedId(@PathVariable Integer id) {
		Map<String, Object> response = new HashMap<>();
		try {
			Integer deletedPostId = postRepository.deleteById(id);
			if (deletedPostId < 0) throw new RuntimeException("Failed! Post was not deleted due to error.");
			response.put("success", true);
			response.put("message", "Post deleted successfully");
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (RuntimeException e) {
			response.put("success", false);
			response.put("message", e.getLocalizedMessage());
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
	}

	@PutMapping("/{id}")
	ResponseEntity<Map<String, Object>> updatePostWithProvidedPayload(@RequestBody PostDTO payload) {
		Map<String, Object> response = new HashMap<>();
		try {
			PostDTO updated = postRepository.updatePostWithProvidedPayload(payload);
			if (updated == null) throw new RuntimeException("failed to update post");
			PostDTO postDTO = postRepository.findById(updated.post_id());
			response.put("success", true);
			response.put("message", "Post updated successfully");
			response.put("data", postDTO);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (RuntimeException e) {
			response.put("success", false);
			response.put("message", "Error: could not update post, please try again");
			return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
		}
	}
}
