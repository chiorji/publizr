package dev.publizr.post;

import dev.publizr.models.APIResponseDTO;
import dev.publizr.post.models.Post;
import dev.publizr.post.models.PostDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/posts")
@CacheEvict(value = "posts", allEntries = true)
public class PostController {

	private final PostRepository postRepository;

	public PostController(PostRepository postRepository) {
		this.postRepository = postRepository;
	}

	@GetMapping("")
	@Operation(
		summary = "Get the list of publications",
		description = "This endpoint returns the list of all the publication on the platform",
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "Publications retrieved",
				content = @Content(schema = @Schema(implementation = APIResponseDTO.class))),
			@ApiResponse(
				responseCode = "400",
				description = "Failed to retieve publications",
				content = @Content
			)
		}
	)
	ResponseEntity<APIResponseDTO<List<PostDTO>>> list() {
		try {
			List<PostDTO> postDTO = postRepository.list();
			APIResponseDTO<List<PostDTO>> responseDTO = new APIResponseDTO<>(true, "publications retrieved", postDTO, postDTO.size());
			return ResponseEntity.ok(responseDTO);
		} catch (RuntimeException e) {
			APIResponseDTO<List<PostDTO>> errorResponse = new APIResponseDTO<>(false, "Error occurred while retrieving publications", null, 0);
			return ResponseEntity.badRequest().body(errorResponse);
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
			response.put("message", "Could not retrieve information for post with ID " + id);
			return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
		}
	}

	@PostMapping("/publish")
	ResponseEntity<Map<String, Object>> create(@RequestBody @Valid Post post) {
		Map<String, Object> response = new HashMap<>();

		try {
			Integer postId = postRepository.save(post);
			PostDTO newPost = postRepository.findById(postId);

			response.put("success", true);
			response.put("data", newPost);
			return new ResponseEntity<>(response, HttpStatus.CREATED);
		} catch (RuntimeException e) {
			response.put("success", false);
			response.put("message", "An error occurred while publishing post");
			return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
		}
	}

	@GetMapping("/author/{id}")
	ResponseEntity<Map<String, Object>> byAuthorId(@PathVariable Integer id) {
		Map<String, Object> response = new HashMap<>();
		try {
			List<PostDTO> postDTO = postRepository.byAuthorId(id);
			response.put("success", true);
			response.put("data", postDTO);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (RuntimeException e) {
			response.put("success", false);
			response.put("message", "Post does not exist or might have been deleted");
			return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
		}
	}

	@GetMapping("/recent")
	ResponseEntity<Map<String, Object>> recent() {
		Map<String, Object> response = new HashMap<>();
		try {
			List<PostDTO> postDTO = postRepository.recent();
			response.put("success", true);
			response.put("data", postDTO);
			response.put("total", postDTO.size());
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (RuntimeException e) {
			response.put("success", false);
			response.put("message", "Error retrieving posts");
			return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
		}
	}

	@DeleteMapping("/{id}")
	ResponseEntity<Map<String, Object>> deleteById(@PathVariable Integer id) {
		Map<String, Object> response = new HashMap<>();
		try {
			Integer deletedPostId = postRepository.deleteById(id);
			if (deletedPostId < 0) throw new RuntimeException("Failed! Post was not deleted due to error.");
			response.put("success", true);
			response.put("message", "Post deleted successfully");
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (RuntimeException e) {
			response.put("success", false);
			response.put("message", "Failed to delete post");
			return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
		}
	}

	@PutMapping("/{id}")
	ResponseEntity<Map<String, Object>> update(@RequestBody @Valid PostDTO postDTO) {
		Map<String, Object> response = new HashMap<>();
		try {
			PostDTO postDTO1 = postRepository.update(postDTO);
			response.put("success", true);
			response.put("message", "Post updated successfully");
			response.put("data", postDTO1);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (RuntimeException e) {
			response.put("success", false);
			response.put("message", "Error: could not update post, please try again");
			return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
		}
	}
}
